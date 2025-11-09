package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class UserdataDaoJdbc implements UserDao {
    private static final Config CFG = Config.getInstance();
    private final Connection connection;
    public UserdataDaoJdbc(Connection connection) {
        this.connection = connection;
    }


    @Override
    public UserEntity create(UserEntity user) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO \"user\" (username, currency, first_name, surname, photo, photo_small, full_name)" +
                            "VALUES ( ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )){
               ps.setString(1, user.getUsername());
               ps.setObject(2,user.getCurrency().name());
               ps.setString(3,user.getFirstname());
               ps.setString(4,user.getSurname());
               ps.setObject(5,user.getPhoto());
               ps.setObject(6,user.getPhotoSmall());
               ps.setString(7,user.getFullname());

               ps.execute();
               final UUID generatedKey;
               try (ResultSet rs = ps.getGeneratedKeys()){
                   if (rs.next()){
                       generatedKey=rs.getObject("id", UUID.class);
                   }else {
                       throw new SQLException("Can`t find id in ResultSet");
                   }

               user.setId(generatedKey);
               return user;
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM \"user\" WHERE \"id\" = ?"
            )) {
                ps.setObject(1, id);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        UserEntity ue = new UserEntity();
                        ue.setId(rs.getObject("id", UUID.class));
                        ue.setUsername(rs.getString("username"));
                        ue.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                        ue.setFirstname(rs.getString("firstname"));
                        ue.setSurname(rs.getString("surname"));
                        ue.setPhoto(rs.getBytes("photo"));
                        ue.setPhotoSmall(rs.getBytes("photo_small"));
                        ue.setFullname(rs.getString("full_name"));
                        return Optional.of(ue);
                    } else {
                        return Optional.empty();
                    }

            }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }



    @Override
    public Optional<UserEntity> findByUsername(String user) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM \"user\" WHERE \"username\" = ?"
            )) {
                ps.setString(1, user);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        UserEntity ue = new UserEntity();
                        ue.setId(rs.getObject("id", UUID.class));
                        ue.setUsername(rs.getString("username"));
                        ue.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                        ue.setFirstname(rs.getString("firstname"));
                        ue.setSurname(rs.getString("surname"));
                        ue.setPhoto(rs.getBytes("photo"));
                        ue.setPhotoSmall(rs.getBytes("photo_small"));
                        ue.setFullname(rs.getString("full_name"));
                        return Optional.of(ue);
                    } else {
                        return Optional.empty();
                    }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UserEntity user) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM \"user\" WHERE \"id\"=?"
            )) {
                preparedStatement.setObject(1, user.getId());
                int deleteCounts = preparedStatement.executeUpdate();
                if (deleteCounts == 0) {
                    throw new SQLException("User not deleted");
                }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
