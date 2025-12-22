package guru.qa.niffler.test.db;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.FriendshipStatus;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Test;

public class UserdataJdbcTest {

    @Test
    public void createUserdataUser() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserdataUser(new UserJson(
                null,
                "mikeUserName_3",
                "testfirstName",
                "TestSurname",
                "testFullname",
                CurrencyValues.RUB,
                null,
                null,
                FriendshipStatus.FRIEND
        ));
        System.out.println(user);
    }
}
