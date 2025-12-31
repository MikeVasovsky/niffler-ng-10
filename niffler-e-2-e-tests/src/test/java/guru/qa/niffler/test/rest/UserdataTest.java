package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersApiClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserdataTest {

    private final UsersApiClient usersApiClient = new UsersApiClient();


    @User
    @Test
    void currentUserTest(UserJson user){
        UserJson userJson = usersApiClient.currentUser(user.username());
        assertEquals(user.username(), userJson.username());
    }

    @Test
    void allUsersTest(){
        String username = "user_2_friendship";
        List<UserJson> result = usersApiClient.findAllUsers(username, "hren_s_gor");
        for(UserJson user: result){
            System.out.println(user);
        }
    }

    @Test
    void updateUserTest() {
        UserJson userJson = new UserJson(
                null,
                "lorrie.smitham",
                "her",
                "her",
                "her_s_gor_update",
                CurrencyValues.EUR,
                null,
                null,
                null,
                null
        );
        usersApiClient.update(userJson);
    }

    @Test
    void testSendInvitation(){
        UserJson user = usersApiClient.currentUser("detra.weissnat");
        UserJson target = usersApiClient.currentUser("marcos.murazik");
        usersApiClient.sendInvitation(user.username(), target.username());
    }

    @User
    @Test
    void acceptFriendshipReq(UserJson userSend){
        UserJson newUser2 = usersApiClient.currentUser(RandomDataUtils.randomUsername());
        usersApiClient.sendInvitation(userSend.username(),newUser2.username());
        usersApiClient.acceptInvitation(newUser2.username(), userSend.username());
    }
}
