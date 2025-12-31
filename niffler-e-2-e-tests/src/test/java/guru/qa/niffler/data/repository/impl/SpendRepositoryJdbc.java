package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class SpendRepositoryJdbc implements SpendRepository {
    private final static Config CFG = Config.getInstance();
    private final static String URL = CFG.spendJdbcUrl();

    @Override
    public SpendEntity create(SpendEntity spend) {
        try (PreparedStatement ps = holder(URL).connection().prepareStatement(
                "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) " +
                        "VALUES ( ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, spend.getUsername());
            ps.setDate(2, new java.sql.Date(spend.getSpendDate().getTime()));
            ps.setString(3, spend.getCurrency().name());
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, spend.getCategory().getId());

            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            spend.setId(generatedKey);
            return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SpendEntity update(SpendEntity spend) {
        try (PreparedStatement ps = holder(URL).connection().prepareStatement(
                """
                        update spend set
                        username = ?,
                        spend_date = ?,
                        currency = ?,
                        amount = ?,
                        description = ?
                        where id = ?
                        """
        )) {
            ps.setString(1, spend.getUsername());
            ps.setDate(2, (Date) spend.getSpendDate());
            ps.setString(3, spend.getCurrency().name());
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, spend.getId());

            ps.executeUpdate();

            return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CategoryEntity update(CategoryEntity category) {
        try (PreparedStatement ps = holder(URL).connection().prepareStatement(
                """
                        update category set
                        name = ?,
                        username = ?
                        where id = ?
                        """
        )) {
            ps.setString(1, category.getName());
            ps.setString(2, category.getUsername());
            ps.setBoolean(3, category.isArchived());

            ps.executeUpdate();

            return category;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        try (PreparedStatement ps = holder(URL).connection().prepareStatement(
                "INSERT INTO category (name, username,archived)" +
                        "VALUES (?,?,?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, category.getName());
            ps.setString(2, category.getUsername());
            ps.setBoolean(3, category.isArchived());

            ps.executeUpdate();
            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
                category.setId(generatedKey);
                return category;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        try (PreparedStatement ps = holder(URL).connection().prepareStatement(
                "select * from category where id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                CategoryEntity category = new CategoryEntity();
                if (rs.next()) {
                    category.setId(id);
                    category.setName(rs.getString("name"));
                    category.setUsername(rs.getString("username"));
                    category.setArchived(rs.getBoolean("archived"));
                }
                if (category == null) {
                    return Optional.empty();
                } else {
                    return Optional.of(category);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String name) {
        try (PreparedStatement ps = holder(URL).connection().prepareStatement(
                """
                        select * from category where username = ? and name = ?
                        """
        )) {
            ps.setString(1, username);
            ps.setString(2, name);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                CategoryEntity category = new CategoryEntity();
                if (rs.next()) {
                    category.setId(rs.getObject("id", UUID.class));
                    category.setName(rs.getString("name"));
                    category.setUsername(rs.getString("username"));
                    category.setArchived(rs.getBoolean("archived"));
                }
                if (category == null) {
                    return Optional.empty();
                } else {
                    return Optional.of(category);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SpendEntity> findById(UUID id) {
        String sql = """
                SELECT s.*, c.id as cat_id, c.name as cat_name, 
                       c.username as cat_username, c.archived as cat_archived
                FROM spend s
                LEFT JOIN category c ON s.category_id = c.id
                WHERE s.id = ?
                """;
        try (PreparedStatement ps = holder(URL).connection().prepareStatement(sql)) {
            ps.setObject(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SpendEntity spend = new SpendEntity();
                    spend.setId(rs.getObject("id", UUID.class));
                    spend.setUsername(rs.getString("username"));
                    spend.setSpendDate(rs.getDate("spend_date"));
                    spend.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    spend.setAmount(rs.getDouble("amount"));
                    spend.setDescription(rs.getString("description"));

                    if (rs.getObject("cat_id") != null) {
                        CategoryEntity category = new CategoryEntity();
                        category.setId(rs.getObject("cat_id", UUID.class));
                        category.setName(rs.getString("cat_name"));
                        category.setUsername(rs.getString("cat_username"));
                        category.setArchived(rs.getBoolean("cat_archived"));
                        spend.setCategory(category);
                    }

                    return Optional.of(spend);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find spend by id: " + id, e);
        }
    }

    @Override
    public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
        try (PreparedStatement ps = holder(URL).connection().prepareStatement(
                """
                        SELECT s.*, c.id as cat_id, c.name as cat_name, 
                               c.username as cat_username, c.archived as cat_archived
                        FROM spend s
                        LEFT JOIN category c ON s.category_id = c.id
                        WHERE s.username = ? AND s.description = ?
                        """
        )) {
            ps.setString(1, username);
            ps.setString(2, description);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SpendEntity spend = new SpendEntity();
                    spend.setId(rs.getObject("id", UUID.class));
                    spend.setUsername(rs.getString("username"));
                    spend.setSpendDate(rs.getDate("spend_date"));
                    spend.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    spend.setAmount(rs.getDouble("amount"));
                    spend.setDescription(rs.getString("description"));

                    if (rs.getObject("cat_id") != null) {
                        CategoryEntity category = new CategoryEntity();
                        category.setId(rs.getObject("cat_id", UUID.class));
                        category.setName(rs.getString("cat_name"));
                        category.setUsername(rs.getString("cat_username"));
                        category.setArchived(rs.getBoolean("cat_archived"));
                        spend.setCategory(category);
                    }
                    return Optional.of(spend);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(SpendEntity spend) {
        try (PreparedStatement ps = holder(URL).connection().prepareStatement(
                "delete from spend where id = ?"
        )) {
            ps.setObject(1, spend.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        try (PreparedStatement ps = holder(URL).connection().prepareStatement(
                "delete from category where id = ?"
        )) {
            ps.setObject(1, category.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CategoryEntity> allCategories(String username) {
        try (PreparedStatement ps = holder(URL).connection().prepareStatement(
                "SELECT * FROM category")) {
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                List<CategoryEntity> res = new ArrayList<>();
                while (rs.next()) {
                    CategoryEntity category = new CategoryEntity();
                    category.setId(rs.getObject("id", UUID.class));
                    category.setName(rs.getString("name"));
                    category.setUsername(rs.getString("username"));
                    category.setArchived(rs.getBoolean("archived"));
                    res.add(category);
                }
               return res;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
