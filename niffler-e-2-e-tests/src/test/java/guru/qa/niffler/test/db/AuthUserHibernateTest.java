package guru.qa.niffler.test.db;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.model.*;
import guru.qa.niffler.service.imp.AuthDbClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.NoSuchElementException;

import static java.util.UUID.fromString;
import static org.junit.jupiter.api.Assertions.*;


public class AuthUserHibernateTest {

    static AuthDbClient authDbClient = new AuthDbClient();

    @ValueSource(strings = {
            "test-create-auser22"
    })
    @ParameterizedTest
    void createAuthUser(String auname) {
        AuthUserJson newAuthUserJson = authDbClient.create(
                auname,
                "12345"
        );
        assertNotNull(newAuthUserJson);
        assertEquals(auname, newAuthUserJson.username());
    }

    @Test
    void findByIdTest() {
        AuthUserJson authUserJson = authDbClient
                .findById(fromString("99446ce9-b065-498a-8be1-36b5c03efa57"));
        assertEquals("99446ce9-b065-498a-8be1-36b5c03efa57", authUserJson.id().toString());
    }

    @ValueSource(strings = {
            "marcell.collins"
    })
    @ParameterizedTest
    void findByUsername(String username) {
        AuthUserJson authUserJson = authDbClient
                .findByUsername(username);
        assertEquals(authUserJson.username(), username);
    }

    @Test
    void updateUserTest() {
        AuthUserEntity authUser = AuthUserEntity.fromJson(new AuthUserJson(
                fromString("8eae05c1-2bbd-4c37-9aa3-ed1440223fea"),
                "test_6.1_jdbc_update_2",
                "{bcrypt}$2a$10$AMEI7wGkYd2.dq5YJCKsRua7yOj.OINNzVqUx.t07TdTQcgVzwlp2",
                true,
                true,
                true,
                true
        ));
        AuthUserJson oldAuthUser = authDbClient.findById(authUser.getId());
        AuthUserJson newUser = authDbClient.updateUser(AuthUserJson.fromEntity(authUser));
        assertNotEquals(newUser, oldAuthUser);
    }

    @Test
    void deleteUser() {
        AuthUserEntity authUser = AuthUserEntity.fromJson(new AuthUserJson(
                fromString("f143d126-2151-4525-ae1f-aaa7fb8c86e7"),
                "jadwiga.fadel",
                "{bcrypt}$2a$10$DlMajxsaPLbmlEPT587z8uPY4BWxszgBIRf7ZYP4JJa9ejiXMDQFq",
                true,
                true,
                true,
                true
        ));
        authDbClient.deleteUser(AuthUserJson.fromEntity(authUser));
        try {
            authDbClient.findById(authUser.getId());
        } catch (NoSuchElementException o) {
            assertEquals(o.toString(), "java.util.NoSuchElementException: No value present");
        }
    }
}
