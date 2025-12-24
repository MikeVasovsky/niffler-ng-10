package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.ACCEPTED;
import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.PENDING;
import static guru.qa.niffler.data.tpl.Connections.holder;

public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

    private static final Config CFG = Config.getInstance();
    private static final String URL = CFG.userdataJdbcUrl();

    @Override
    public UserEntity create(UserEntity user) {
        try (PreparedStatement ps = holder(URL).connection().prepareStatement(
                "INSERT INTO \"user\" (username, currency) VALUES (?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.executeUpdate();

            final UUID generatedUserId;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedUserId = rs.getObject("id", UUID.class);
                } else {
                    throw new IllegalStateException("Can`t find id in ResultSet");
                }
            }
            user.setId(generatedUserId);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(URL).connection().prepareStatement("SELECT * FROM \"user\" WHERE id = ? ")) {
            ps.setObject(1, id);

            ps.execute();
            ResultSet rs = ps.getResultSet();

            if (rs.next()) {
                UserEntity result = new UserEntity();
                result.setId(rs.getObject("id", UUID.class));
                result.setUsername(rs.getString("username"));
                result.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                result.setFirstname(rs.getString("firstname"));
                result.setSurname(rs.getString("surname"));
                result.setPhoto(rs.getBytes("photo"));
                result.setPhotoSmall(rs.getBytes("photo_small"));
                return Optional.of(result);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try (PreparedStatement ps = holder(URL).connection().prepareStatement("SELECT * FROM \"user\" WHERE username = ? ")) {
            ps.setObject(1, username);

            ps.execute();
            ResultSet rs = ps.getResultSet();

            if (rs.next()) {
                UserEntity result = new UserEntity();
                result.setId(rs.getObject("id", UUID.class));
                result.setUsername(username);
                result.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                result.setFirstname(rs.getString("firstname"));
                result.setSurname(rs.getString("surname"));
                result.setPhoto(rs.getBytes("photo"));
                result.setPhotoSmall(rs.getBytes("photo_small"));
                return Optional.of(result);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity update(UserEntity user) {
        try (PreparedStatement ps = holder(URL).connection().prepareStatement(
                """
                        update \"user\"  set username = ?
                        , currency = ?
                        , firstname = ?
                        , surname = ?
                        , photo = ?
                        , photo_small = ?
                        , full_name = ?
                        where id = ?
                        """
        )) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.setString(3, user.getFirstname());
            ps.setString(4, user.getSurname());
            ps.setObject(5, user.getPhoto());
            ps.setObject(6, user.getPhotoSmall());
            ps.setObject(7, user.getId());

            ps.executeUpdate();

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendInvitation(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement reqPs = holder(URL).connection().prepareStatement(
                "INSERT INTO \"friendship\" (requester_id, addressee_id, status) VALUES (?, ?, ?)"
        )
        ) {
            reqPs.setObject(1, requester.getId());
            reqPs.setObject(2, addressee.getId());
            reqPs.setObject(3, PENDING.toString());

            reqPs.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement reqPs = holder(URL).connection().prepareStatement(
                "INSERT INTO \"friendship\" (requester_id, addressee_id, status) VALUES (?, ?, ?)"
        );
             PreparedStatement addPs = holder(URL).connection().prepareStatement(
                     "INSERT INTO \"friendship\" (requester_id, addressee_id, status) VALUES (?, ?, ?)"
             )) {
            reqPs.setObject(1, requester.getId());
            reqPs.setObject(2, addressee.getId());
            reqPs.setObject(3, ACCEPTED.toString());

            addPs.setObject(1, addressee.getId());
            addPs.setObject(2, requester.getId());
            addPs.setObject(3, ACCEPTED.toString());

            reqPs.execute();
            addPs.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(UserEntity user) {
        try (PreparedStatement ps = holder(URL).connection().prepareStatement(
                "delete from \"user\" where id = ?"
        );
             PreparedStatement delReq = holder(URL).connection().prepareStatement(
                     "delete from friendship where requester_id = ?"
             );
             PreparedStatement delAdd = holder(URL).connection().prepareStatement(
                     "delete from friendship where addressee_id = ?"
             )) {
            ps.setObject(1, user.getId());
            delReq.setObject(1, user.getId());
            delAdd.setObject(1, user.getId());

            ps.execute();
            delReq.execute();
            delAdd.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
