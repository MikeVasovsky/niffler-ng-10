package guru.qa.niffler.test.db;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.imp.UserdataDbClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static guru.qa.niffler.model.CurrencyValues.RUB;
import static java.util.UUID.fromString;

public class UserdataHibernateTest {
    static UserdataDbClient userdataDbClient = new UserdataDbClient();

    @ValueSource(strings = {
            "wow_test_111"
    })
    @ParameterizedTest
    void createUser(String uname) {
        userdataDbClient.createUser(
                uname,
                "12345"
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "3b123e92-6b72-4038-9369-bc8de64452ed"
    })
    void findUserBuId(UUID uuid) {
        UserJson userJson =
                userdataDbClient.findById(uuid);
        System.out.println(userJson);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "valentin-pppp111"
    })
    void findByUsername(String username) {
        UserJson userJson =
                userdataDbClient.findByUsername(username);
        System.out.println(userJson);
    }

    @Test
    void updateUser() {
        UserEntity user = UserEntity.fromJson(new UserJson(
                fromString("74030e8c-dd78-11f0-94c8-0242ac110002"),
                "wow_test",
                "just_a_man",
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
                fromString("c1cfdf04-dd79-11f0-9897-0242ac110002"),
                "wow_test_111",
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
                fromString("4565362b-bbec-4cfb-b64c-174cffb554e1"),
                "valentin-pppp111",
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
                fromString("f2caa94e-08e8-4c53-b052-2b6b184e9156"),
                "rayna.hintz",
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
