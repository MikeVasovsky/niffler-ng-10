package guru.qa.niffler.test.db;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.FriendshipStatus;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Test;

public class UserJdbcTest {

    @Test
    public void createFullUser() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUser(new UserJson(
                null,
                "mikeUserName_000",
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

    @Test
    public void createUserChained() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserChained(new UserJson(
                null,
                "mikeUserName_00",
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
