package guru.qa.niffler.service;

import guru.qa.niffler.api.UserdataApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsersApiClient implements UsersClient {

    private static final Config CFG = Config.getInstance();

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CFG.userdataUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final UserdataApi userdataApi = retrofit.create(UserdataApi.class);

    @Override
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

    @Override
    public UserJson createUser(String username, String password) {
        throw new UnsupportedOperationException("Данный метод не реализован");
    }

    @Override
    public List<UserJson> addIncomeInvitation(UserJson targetUser, int count) {
        throw new UnsupportedOperationException("Данный метод не реализован");
    }

    @Override
    public List<UserJson> addOutcomeInvitation(UserJson targetUser, int count) {
        throw new UnsupportedOperationException("Данный метод не реализован");
    }

    @Override
    public List<UserJson> addFriend(UserJson targetUser, int count) {
        throw new UnsupportedOperationException("Данный метод не реализован");
    }

    @Override
    public UserJson findById(UUID id) {
        throw new UnsupportedOperationException("Данный метод не реализован");
    }

    @Override
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


    @Override
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

    @Override
    public void sendInvitation(UserJson targetUser, int count) {
        throw new UnsupportedOperationException("Данный метод не реализован");
    }

    @Override
    public UserJson sendInvitation(String username, String targetUser) {
        Response<UserJson> response;
        try {
            response = userdataApi.sendInvitation(username, targetUser)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Override
    public UserJson acceptInvitation(String username, String targetUser) {
        Response<UserJson> response;
        try {
            response = userdataApi.acceptInvitation(username, targetUser)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }


    @Override
    public void remove(UserJson user) {
        throw new UnsupportedOperationException("Данный метод не реализован");
    }

    @Override
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

    @Override
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

    @Override
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
}
