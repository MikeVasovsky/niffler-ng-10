package guru.qa.niffler.data.mapper.springmapper;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserdataFriendshipResultSetExtractor implements ResultSetExtractor<List<FriendshipEntity>> {

    public static final UserdataFriendshipResultSetExtractor instance = new UserdataFriendshipResultSetExtractor();

    private UserdataFriendshipResultSetExtractor(){}

    @Override
    public List<FriendshipEntity> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<FriendshipEntity> friendshipList = new ArrayList<>();
        while (rs.next()){
            UserEntity requester = new UserEntity();
            requester.setId(rs.getObject("r_id", UUID.class));
            requester.setUsername(rs.getString("r_username"));
            requester.setCurrency(CurrencyValues.valueOf(rs.getString("r_currency")));

            UserEntity addressee = new UserEntity();
            addressee.setId(rs.getObject("a_id", UUID.class));
            addressee.setUsername(rs.getString("a_username"));
            addressee.setCurrency(CurrencyValues.valueOf(rs.getString("a_currency")));

            FriendshipEntity fe = new FriendshipEntity();
            fe.setRequester(requester);
            fe.setAddressee(addressee);
            fe.setStatus(FriendshipStatus.valueOf(rs.getString("status")));
            fe.setCreatedDate(rs.getDate("created_date"));
            friendshipList.add(fe);
        }
        return friendshipList;
    }
}
