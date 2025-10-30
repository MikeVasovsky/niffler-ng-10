package guru.qa.niffler.service;

import guru.qa.niffler.data.UserEntity;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.dao.impl.UsernameDaoJdbc;
import guru.qa.niffler.model.UserJson;

import java.util.Optional;

public class UserDbClient implements UserDao {


    private UserDao userDbClient = new UsernameDaoJdbc();

    public UserJson createUser(UserJson user) {
        UserEntity ue = UserEntity.fromJson(user);
        return UserJson.fromEntity(
                userDbClient.create(ue)
        );
    }

    public UserJson findUserByUsername(UserJson user) {
        UserEntity ue = UserEntity.fromJson(user);
        Optional<UserEntity> findUser = userDbClient.findByUsername(ue.getUsername());
        UserEntity userEntity = findUser.orElseThrow();

        return UserJson.fromEntity(userEntity);
    }

    public UserJson findById(UserJson user) {
        UserEntity ue = UserEntity.fromJson(user);
        Optional<UserEntity> findUser = userDbClient.findById(ue.getId());
        UserEntity userEntity = findUser.orElseThrow();

        return UserJson.fromEntity(userEntity);
    }

    public void deleteUser(UserJson user) {
        UserEntity ue = UserEntity.fromJson(user);

        userDbClient.delete(ue);
    }
}
