package guru.qa.niffler.test.db;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.model.*;
import guru.qa.niffler.service.imp.AuthDbClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static java.util.UUID.fromString;


public class AuthUserHibernateTest {

    static AuthDbClient authDbClient = new AuthDbClient();

    @ValueSource(strings = {
            "test-create-auser"
    })
    @ParameterizedTest
    void createAuthUser(String auname) {
        authDbClient.create(
                auname,
                "12345"
        );
    }

    @Test
    void findByIdTest() {
        AuthUserJson authUserJson = authDbClient
                .findById(fromString("99446ce9-b065-498a-8be1-36b5c03efa57"));
        System.out.println(authUserJson);
    }

    @Test
    void findByUsername() {
        AuthUserJson authUserJson = authDbClient
                .findByUsername("user_2_friendship");
        System.out.println(authUserJson);
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
        authDbClient.updateUser(authUser);
    }

    @Test
    void deleteUser() {
        AuthUserEntity authUser = AuthUserEntity.fromJson(new AuthUserJson(
                fromString("d54105e1-8e70-4153-b74f-e640c66673d5"),
                "marianne.glover",
                "{bcrypt}$2a$10$JRGnkqA572Ut7wEdz3rAke3J7ds50toYSMDbr7iSndJGeB2M95N/y.",
                true,
                true,
                true,
                true
        ));
        authDbClient.deleteUser(authUser);
    }
}
