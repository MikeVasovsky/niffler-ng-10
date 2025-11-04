package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoJdbc implements SpendDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public SpendEntity create(SpendEntity spend) {
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) " +
                            "VALUES ( ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                ps.setString(1, spend.getUsername());
                ps.setDate(2, spend.getSpendDate());
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
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SpendEntity> findById(UUID id) {
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM spend WHERE id = ?"
            )) {
                ps.setObject(1, id);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        SpendEntity se = new SpendEntity();
                        CategoryEntity category =  new CategoryEntity();
                        category.setId(rs.getObject("category_id", UUID.class));
                        se.setId(rs.getObject("id", UUID.class));
                        se.setSpendDate(rs.getDate("spend_date"));
                        se.setCategory(category);
                        se.setUsername(rs.getString("username"));
                        se.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                        se.setAmount(rs.getDouble("amount"));
                        se.setDescription(rs.getString("description"));

                        return Optional.of(se);
                    } else {
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        List<SpendEntity> allUsers = new ArrayList<>();
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * from spend WHERE username = ?"
            )) {
                preparedStatement.setString(1, username);
                preparedStatement.execute();
                try (ResultSet rs = preparedStatement.getResultSet()) {
                    while (rs.next()) {
                      SpendEntity se = new SpendEntity();
                      CategoryEntity category =  new CategoryEntity();
                      category.setId(rs.getObject("category_id", UUID.class));
                      se.setId(rs.getObject("id", UUID.class));
                      se.setSpendDate(rs.getDate("spend_date"));
                      se.setCategory(category);
                      se.setUsername(rs.getString("username"));
                      se.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                      se.setAmount(rs.getDouble("amount"));
                      se.setDescription(rs.getString("description"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allUsers;
    }

    @Override
    public void delete(SpendEntity spend) {
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM spend WHERE id=?"
            )) {
                preparedStatement.setObject(1, spend.getId());
                int deleteCounts = preparedStatement.executeUpdate();
                if (deleteCounts == 0) {
                    throw new SQLException("Spend not deleted");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}