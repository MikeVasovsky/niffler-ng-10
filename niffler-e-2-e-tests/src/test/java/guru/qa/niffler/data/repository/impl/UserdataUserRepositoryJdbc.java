package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

  private final UserdataUserDao udUserDao = new UserdataUserDaoJdbc();

  @Step("Создать пользователя")
  @Nonnull
  @Override
  public UserEntity create(UserEntity user) {
    return udUserDao.create(user);
  }

  @Step("Изменить пользователя")
  @Nonnull
  @Override
  public UserEntity update(UserEntity user) {
    return udUserDao.update(user);
  }

  @Step("Найти пользователя по id")
  @Nonnull
  @Override
  public Optional<UserEntity> findById(UUID id) {
    return udUserDao.findById(id);
  }

  @Step("Найти пользователя по имени")
  @Nonnull
  @Override
  public Optional<UserEntity> findByUsername(String username) {
    return udUserDao.findByUsername(username);
  }

  @Step("Добавить заявку в друзья")
  @Override
  public void addFriendshipRequest(UserEntity requester, UserEntity addressee) {
    requester.addFriends(FriendshipStatus.PENDING, addressee);
    udUserDao.update(requester);
  }

  @Step("Добавить друга")
  @Override
  public void addFriend(UserEntity requester, UserEntity addressee) {
    requester.addFriends(FriendshipStatus.ACCEPTED, addressee);
    addressee.addFriends(FriendshipStatus.ACCEPTED, requester);
    udUserDao.update(requester);
    udUserDao.update(addressee);
  }
}