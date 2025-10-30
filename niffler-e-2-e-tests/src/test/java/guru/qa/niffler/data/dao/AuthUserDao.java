package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.userdata.UserEntity;

public interface AuthUserDao {
    UserEntity create(UserEntity userEntity);
}
