package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;

import java.util.List;
import java.util.UUID;

public interface UsersClient {

    UserJson currentUser(String username);

    UserJson createUser(String username, String password);

    List<UserJson> addIncomeInvitation(UserJson targetUser, int count);

    List<UserJson> addOutcomeInvitation(UserJson targetUser, int count);

    List<UserJson> addFriend(UserJson targetUser, int count);

    UserJson findById(UUID id);

    List<UserJson> findAllUsers(String username, String searchQuery);

    UserJson findByUsername(String username);

    UserJson update(UserJson user);

    void sendInvitation(UserJson targetUser, int count);

    UserJson sendInvitation(String username, String targetUser);

    UserJson acceptInvitation(String username, String targetUser);

    void remove(UserJson user);

    UserJson declineInvitation(String username, String targetUsername);

    List<UserJson> getAllFerinds(String username, String searchQuery);

    void removeFriends(String username, String targetUsername);
}
