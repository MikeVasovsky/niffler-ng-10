package guru.qa.niffler.data.mapper.springmapper;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserdataUserResultSetExtractor implements ResultSetExtractor<List<UserEntity>> {

    public static final UserdataUserResultSetExtractor instance = new UserdataUserResultSetExtractor();

    private UserdataUserResultSetExtractor() {
    }

    @Override
    public List<UserEntity> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<UserEntity> userEntityList = new ArrayList<>();
        while (rs.next()) {
            UserEntity user = new UserEntity();
            user.setId(rs.getObject("id", UUID.class));
            user.setUsername(rs.getString("username"));
            user.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
            user.setFirstname(rs.getString("firstname"));
            user.setSurname(rs.getString("surname"));
            user.setPhoto(rs.getBytes("photo"));
            user.setPhotoSmall(rs.getBytes("photo_small"));
            user.setFullname(rs.getString("full_name"));
            userEntityList.add(user);
        }
        return userEntityList;
    }
}
