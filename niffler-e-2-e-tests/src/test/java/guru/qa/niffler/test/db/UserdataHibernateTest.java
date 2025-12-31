package guru.qa.niffler.test.db;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.jupiter.annotation.NewUser;
import guru.qa.niffler.model.NewUserModel;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.imp.UserdataDbClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.NoSuchElementException;
import java.util.UUID;

import static guru.qa.niffler.model.CurrencyValues.RUB;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static java.util.UUID.fromString;
import static org.junit.jupiter.api.Assertions.*;

public class UserdataHibernateTest {
    static UserdataDbClient userdataDbClient = new UserdataDbClient();
    private final String password = "12345";

    @ValueSource(strings = {
            "wow_test_11111",
            "user_1+friendship"
    })
    @ParameterizedTest
    void createUser(String uname) {
        UserJson userJson = userdataDbClient.createUser(
                uname,
                password
        );
        assertNotNull(userJson);
        assertEquals(uname, userJson.username());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "3b123e92-6b72-4038-9369-bc8de64452ed",
            "fd643de8-e527-4c60-a956-0b773c12ed99"
    })
    void findUserBuId(UUID uuid) {
        UserJson userJson =
                userdataDbClient.findById(uuid);
        assertEquals(userJson.id(), uuid);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "valentin-pppp111",
            "user_2_friendship"
    })
    void findByUsername(String username) {
        UserJson userJson =
                userdataDbClient.findByUsername(username);
        assertEquals(userJson.username(), username);
    }

    @Test
    @NewUser
    void updateUser(NewUserModel user) {
        UserJson newUser = userdataDbClient.createUser(user.getName(),user.getPassword());
        UserEntity user1 = UserEntity.fromJson(new UserJson(
                newUser.id(),
                randomUsername(),
                "just_a_man",
                null,
                null,
                RUB,
                null,
                null,
                null
        ));
        UserJson oldUser = userdataDbClient.findById(user1.getId());
        UserJson editUser = userdataDbClient.update(UserJson.fromEntity(user1, null));
        assertNotEquals(oldUser, editUser);
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
        userdataDbClient.remove(UserJson.fromEntity(user, null));
        try {
            userdataDbClient.findById(user.getId());
        } catch (NoSuchElementException o) {
            assertEquals(o.toString(), "java.util.NoSuchElementException: No value present");
        }
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
