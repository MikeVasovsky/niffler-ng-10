package guru.qa.niffler.test.fake;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.model.*;
import guru.qa.niffler.service.imp.AuthDbClient;
import guru.qa.niffler.service.imp.SpendDbClient;
import guru.qa.niffler.service.imp.UserdataDbClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;

import static java.util.UUID.fromString;


public class JdbcHibernateTest {

    @Test
    void txTest() {
        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "cat-name-tx-3",
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "spend-name-tx-3",
                        "duck"
                )
        );

        System.out.println(spend);
    }


    static AuthDbClient authDbClient = new AuthDbClient();
    static UserdataDbClient userdataDbClient = new UserdataDbClient();

    @ValueSource(strings = {
            "valentin-1340"
    })
    @ParameterizedTest
    void springJdbcTest(String uname) {
        UserJson user = userdataDbClient.createUser(
                uname,
                "12345"
        );

        userdataDbClient.addIncomeInvitation(user, 1);
        userdataDbClient.addOutcomeInvitation(user, 1);
    }

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
                .findById(fromString("6e51d1db-a778-42f0-bf61-84f4fb1beb45"));
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
                fromString("940466a7-7359-4fb4-bf4c-7ebfd36431de"),
                "test_6.1__teeest",
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
                fromString("4f387bf2-b6b7-45ab-91a7-555c1bad1278"),
                "marcos.murazik",
                "$10$xdWWPcWKIBqtlQIc101U1Oiq.sfk9ytfVtpKlRPsvZHXEZ0u4Mxsu",
                true,
                true,
                true,
                true
        ));
        authDbClient.deleteUser(authUser);
    }
}
