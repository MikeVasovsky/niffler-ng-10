package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.mapper.springmapper.UserdataUserResultSetExtractor;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.PENDING;

public class UserdataUserRepositorySpringJdbc implements UserdataUserRepository {

    private static final Config CFG = Config.getInstance();
    private static final String URL = CFG.userdataUrl();

    @Override
    public UserEntity create(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(URL));
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) " +
                            "VALUES (?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.setString(3, user.getFirstname());
            ps.setString(4, user.getSurname());
            ps.setBytes(5, user.getPhoto());
            ps.setBytes(6, user.getPhotoSmall());
            ps.setString(7, user.getFullname());
            return ps;
        }, kh);

        final UUID generatedKey = (UUID) kh.getKeys().get("id");
        user.setId(generatedKey);
        return user;
    }

    @Override
    public List<UserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(URL));

        return jdbcTemplate.query(
                "select * from \"user\" u join friendship f on u.id  = f.requester_id  where u.id = ? ",
                UserdataUserResultSetExtractor.instance, id
        );
    }

    @Override
    public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(URL));
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO \"friendship\" (requester_id, addressee_id, status)" +
                            "VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setObject(3, PENDING);
            return ps;

        });
        FriendshipEntity fe = new FriendshipEntity();
        fe.setRequester(requester);
        fe.setAddressee(addressee);
        fe.setStatus(PENDING);
        fe.setCreatedDate(new java.util.Date());

        requester.getFriendshipRequests().add(fe);
        addressee.getFriendshipAddressees().add(fe);
    }

    @Override
    public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {
        addIncomeInvitation(addressee, requester);
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(URL));

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE \"friendship\" SET status = ? WHERE requester_id = ? AND addressee_id = ?"
            );
            ps.setString(1, FriendshipStatus.ACCEPTED.name());
            ps.setObject(2, requester.getId());
            ps.setObject(3, addressee.getId());
            return ps;
        });


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
    }
}
