package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthAutorityUserEntity;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.sql.Connection;
import java.util.function.Function;

import static guru.qa.niffler.data.Databases.transaction;
import static guru.qa.niffler.data.Databases.xaTransaction;


public class AuthUserClient implements AuthAuthorityDao, AuthUserDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public AuthAutorityUserEntity create(AuthAutorityUserEntity authUser) {
        return transaction(
                (Function<Connection, AuthAutorityUserEntity>) connection ->
                        new AuthAuthorityDaoJdbc(connection).create(authUser),
                CFG.authJdbcUrl(),
                1
        );
    }

    @Override
    public UserEntity create(UserEntity userEntity) {
        return transaction(
                (Function<Connection, UserEntity>) connection ->
                        new AuthUserDaoJdbc(connection).create(userEntity),
                CFG.userdataJdbcUrl(),
                1
        );
    }

    public void createUsersInAuthAuthororiryAndUserdataBases(AuthAutorityUserEntity aUser, UserEntity user) {
        Databases.XaConsumer createAuthorityUser = new Databases.XaConsumer(
                connection -> new AuthAuthorityDaoJdbc(connection).create(aUser)
                , CFG.authJdbcUrl(), 1
        );
        Databases.XaConsumer createUserEntity = new Databases.XaConsumer(
                connection -> new AuthUserDaoJdbc(connection).create(user)
                , CFG.userdataJdbcUrl(), 1
        );
        xaTransaction(createAuthorityUser, createUserEntity);
    }



}
