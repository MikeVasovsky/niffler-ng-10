package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
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

import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.PENDING;
import static guru.qa.niffler.data.tpl.Connections.holder;

public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

    private static final String URL = Config.getInstance().userdataJdbcUrl();

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
    public List<UserEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(URL).connection().prepareStatement(
                "select * from \"user\" u join friendship f on u.id =f.addressee_id where f.addressee_id  = ? ")) {
            ps.setObject(1, id);

            ps.execute();
            ResultSet rs = ps.getResultSet();
            List<UserEntity> userEntityList = new ArrayList<>();
            if (rs.next()) {
                UserEntity result = new UserEntity();
                result.setId(rs.getObject("id", UUID.class));
                result.setUsername(rs.getString("username"));
                result.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                result.setFirstname(rs.getString("firstname"));
                result.setSurname(rs.getString("surname"));
                result.setPhoto(rs.getBytes("photo"));
                result.setPhotoSmall(rs.getBytes("photo_small"));
                userEntityList.add(result);
            }
            return userEntityList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement ps = holder(URL).connection().prepareStatement(
                "INSERT INTO \"friendship\" (requester_id, addressee_id, status) VALUES (?, ?, ?)"
        )) {
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, PENDING.toString());
            ps.executeUpdate();

            FriendshipEntity fe = new FriendshipEntity();
            fe.setRequester(requester);
            fe.setAddressee(addressee);
            fe.setStatus(PENDING);
            fe.setCreatedDate(new java.util.Date());

            requester.getFriendshipRequests().add(fe);
            addressee.getFriendshipAddressees().add(fe);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {
        addIncomeInvitation(addressee, requester);
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement ps = holder(URL).connection().prepareStatement(
                "UPDATE  \"friendship\" SET status = ? WHERE requester_id = ? AND addressee_id= ?"
        )) {
            ps.setString(1, FriendshipStatus.ACCEPTED.name());
            ps.setObject(2, requester.getId());
            ps.setObject(3, addressee.getId());
            ps.executeUpdate();

            for (FriendshipEntity fe : requester.getFriendshipRequests()) {
                if (fe.getAddressee().getId().equals(addressee.getId())) {
                    fe.setStatus(FriendshipStatus.ACCEPTED);
                }
            }

            for (FriendshipEntity fe : addressee.getFriendshipAddressees()) {
                if (fe.getRequester().getId().equals(requester.getId())) {
                    fe.setStatus(FriendshipStatus.ACCEPTED);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
