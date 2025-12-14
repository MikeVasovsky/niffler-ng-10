package guru.qa.niffler.service;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.UserJson;

import java.util.UUID;

public interface AuthClient {

    AuthUserJson create(String username, String password);

    AuthUserJson updateUser(AuthUserEntity user);

    AuthUserJson findById(UUID id);

    AuthUserJson findByUsername(String username);

    void deleteUser(AuthUserEntity user);
}

