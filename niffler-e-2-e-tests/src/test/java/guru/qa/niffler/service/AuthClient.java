package guru.qa.niffler.service;

import guru.qa.niffler.model.AuthUserJson;

import java.util.UUID;

public interface AuthClient {

    AuthUserJson create(String username, String password);

    AuthUserJson updateUser(AuthUserJson user);

    AuthUserJson findById(UUID id);

    AuthUserJson findByUsername(String username);

    void deleteUser(AuthUserJson user);
}

