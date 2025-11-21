package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataFriendshipRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UserdataFriendshipRepositoryJdbc implements UserdataFriendshipRepository {

    private static final Config CFG = Config.getInstance();
    private static final String URL = CFG.userdataUrl();

    @Override
    public List<FriendshipEntity> getAllFriendshipRequestById(UUID id) {
        try (PreparedStatement ps = holder(URL).connection().prepareStatement(
                    "select * from \"user\" u join \"friendship\" f on u.id  = f.requester_id where u.id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                List<FriendshipEntity> resultFriendship = new ArrayList<>();
                while (rs.next()) {
                    UserEntity us = new UserEntity();
                    us.setId(rs.getObject("id", UUID.class));

                    FriendshipEntity fe = new FriendshipEntity();
                    fe.setRequester(us);
                    UUID addresseeId = rs.getObject("addressee_id", UUID.class);

                    UserEntity addressee = new UserEntity();
                    addressee.setId(addresseeId);

                    fe.setAddressee(addressee);

                    fe.setStatus(FriendshipStatus.valueOf(rs.getString("status")));

                    fe.setCreatedDate(rs.getDate("created_date"));


                    resultFriendship.add(fe);
                }
                return resultFriendship;

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}