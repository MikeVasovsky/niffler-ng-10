package guru.qa.niffler.service;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.UserJson;

import java.util.UUID;

public interface UserdataClient {

  UserJson createUser(String username, String password);

  UserJson findById(UUID id);

  UserJson findByUsername(String username);

  UserJson update(UserEntity user);

  void addIncomeInvitation(UserJson targetUser, int count);

  void addOutcomeInvitation(UserJson targetUser, int count);

  void addFriend(UserJson targetUser, int count);

  void remove(UserEntity user);

}
