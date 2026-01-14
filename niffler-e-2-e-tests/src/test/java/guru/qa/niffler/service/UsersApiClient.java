package guru.qa.niffler.service;

import guru.qa.niffler.api.UserdataApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsersApiClient implements UsersClient {

    private static final Config CFG = Config.getInstance();
    private static final String PWD = "12345";

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CFG.userdataUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final UserdataApi userdataApi = retrofit.create(UserdataApi.class);

    @NotNull
    @Override
    @Step("Создать нового пользователя")
    public UserJson createUser(String username, String password) {
        try {
            UserJson newUser = new UserJson(
                    null,
                    username,
                    null,
                    null,
                    null,
                    CurrencyValues.RUB,
                    null,
                    null,
                    null,
                    null
            ).addTestData(new TestData(password, null, null, null, null, null));

            Response<UserJson> response = userdataApi.updateUserInfo(newUser).execute();
            assertEquals(200, response.code());

            return response.body().addTestData(new TestData(
                    password,
                    null,
                    null,
                    null,
                    null,
                    null
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public UserJson findByUsername(String username) {
        final Response<UserJson> response;
        try {
            response = userdataApi.currentUser(username).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public List<UserJson> findAllUsers(String username, String searchQuery) {
        Response<List<UserJson>> response;
        try {
            response = userdataApi.allUsers(username, searchQuery)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public UserJson update(UserJson user) {
        Response<UserJson> response;
        try {
            response = userdataApi.updateUserInfo(user)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }


    @NotNull
    @Override
    public List<UserJson> addIncomeInvitation(UserJson targetUser, int count) {
        final List<UserJson> result = new ArrayList<>();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final String username = randomUsername();
                final Response<UserJson> response;
                final UserJson newUser;
                try {
                    newUser = createUser(username, PWD);
                    result.add(newUser);
                    response = userdataApi.sendInvitation(
                            newUser.username(),
                            targetUser.username()
                    ).execute();
                } catch (IOException e) {
                    throw new AssertionError(e);
                }
                assertEquals(200, response.code());

                targetUser.testData()
                        .incomeInvitations()
                        .add(newUser);
            }
        }
        return result;
    }

    @Override
    public List<UserJson> addOutcomeInvitation(UserJson targetUser, int count) {
        final List<UserJson> result = new ArrayList<>();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final String username = randomUsername();
                final Response<UserJson> response;
                final UserJson newUser;
                try {
                    newUser = createUser(username, PWD);
                    result.add(newUser);
                    response = userdataApi.sendInvitation(
                            targetUser.username(),
                            newUser.username()
                    ).execute();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                assertEquals(200, response.code());

                targetUser.testData()
                        .outcomeInvitations()
                        .add(newUser);
            }
        }
        return result;
    }

    @Override
    public List<UserJson> addFriend(UserJson targetUser, int count) {
        final List<UserJson> result = new ArrayList<>();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final String username = randomUsername();
                final Response<UserJson> response;
                final UserJson newUser;
                try {
                    newUser = createUser(username, PWD);
                    result.add(newUser);
                    userdataApi.sendInvitation(
                            newUser.username(),
                            targetUser.username()
                    ).execute();
                    response = userdataApi.acceptInvitation(targetUser.username(), username).execute();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                assertEquals(200, response.code());

                targetUser.testData()
                        .friends()
                        .add(response.body());
            }
        }
        return result;
    }

    public void removeFriends(String username, String targetUsername) {
        Response<Void> response;
        try {
            response = userdataApi.removeFriends(username, targetUsername)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code());
    }

    public List<UserJson> getAllFriends(String username, String searchQuery) {
        Response<List<UserJson>> response;
        try {
            response = userdataApi.friends(username, searchQuery)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public UserJson declineInvitation(String username, String targetUsername) {
        Response<UserJson> response;
        try {
            response = userdataApi.declineInvitation(username, targetUsername)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

}
