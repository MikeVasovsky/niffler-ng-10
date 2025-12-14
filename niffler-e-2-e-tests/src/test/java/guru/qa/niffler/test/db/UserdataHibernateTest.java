package guru.qa.niffler.test.db;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.imp.AuthDbClient;
import guru.qa.niffler.service.imp.UserdataDbClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static guru.qa.niffler.model.CurrencyValues.RUB;
import static java.util.UUID.fromString;

public class UserdataHibernateTest {
    static AuthDbClient authDbClient = new AuthDbClient();
    static UserdataDbClient userdataDbClient = new UserdataDbClient();

    @ValueSource(strings = {
            "valentin-pppp11"
    })
    @ParameterizedTest
    void createUser(String uname) {
        UserJson user = userdataDbClient.createUser(
                uname,
                "12345"
        );

        userdataDbClient.sendInvitation(user, 1);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "dcad4ff8-b686-4cd6-85cc-b9e3850bc858"
    })
    void findUserBuId(UUID uuid) {
        UserJson userJson =
                userdataDbClient.findById(uuid);
        System.out.println(userJson);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "valentin-pppp1"
    })
    void findByUsername(String username) {
        UserJson userJson =
                userdataDbClient.findByUsername(username);
        System.out.println(userJson);
    }

    @Test
    void updateUser() {
        UserEntity user = UserEntity.fromJson(new UserJson(
                fromString("cffdc150-9c12-4a15-be02-ca6a759a4aa9"),
                "test_6.1_update",
                null,
                null,
                null,
                RUB,
                null,
                null,
                null
        ));
        userdataDbClient.update(user);
    }

    @Test
    void deleteUser() {
        UserEntity user = UserEntity.fromJson(new UserJson(
                fromString("71847cfb-6ddd-46a2-8fc5-468cd867c9cb"),
                "valentin-pppp1",
                null,
                null,
                null,
                RUB,
                null,
                null,
                null
        ));
        userdataDbClient.remove(user);
    }

    @Test
    void addFriend() {
        userdataDbClient.addFriend(new UserJson(
                fromString("dcad4ff8-b686-4cd6-85cc-b9e3850bc858"),
                "valentin-1340",
                null,
                null,
                null,
                RUB,
                null,
                null,
                null
        ), 1);
    }

    @Test
    void sendInvitation() {
        userdataDbClient.sendInvitation(new UserJson(
                fromString("7bc2b581-862c-4ea5-a3c9-7bd07bb85a66"),
                "valentin-pppp11",
                null,
                null,
                null,
                RUB,
                null,
                null,
                null
        ), 1);
    }
}
