package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Authority;
import guru.qa.niffler.data.AuthorityEntity;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthAuthorityUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.UserdataDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthAutorityUserEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

import static guru.qa.niffler.data.Databases.transaction;
import static guru.qa.niffler.data.Databases.xaTransaction;

public class UserDbClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();


    public UserJson createUser(UserJson user) {
        UserEntity userEntity = xaTransaction(
                new Databases.XaFunction<>(
                        con -> {
                            AuthAutorityUserEntity authUser = new AuthAutorityUserEntity();
                            authUser.setUsername(user.username());
                            authUser.setPassword(pe.encode("12345"));
                            authUser.setEnabled(true);
                            authUser.setAccountNonExpired(true);
                            authUser.setAccountNonLocked(true);
                            authUser.setCredentialsNonExpired(true);
                            new AuthAuthorityUserDaoJdbc(con).create(authUser);
                            new AuthAuthorityDaoJdbc(con).create(
                                    Arrays.stream(Authority.values())
                                            .map(a -> {
                                                        AuthorityEntity ae = new AuthorityEntity();
                                                        ae.setId(authUser.getId());
                                                        ae.setAuthority(a);
                                                        return ae;
                                                    }
                                            ).toArray(AuthorityEntity[]::new));
                            return null;
                        },
                        CFG.authJdbcUrl(), 1
                ),
                new Databases.XaFunction<>(
                        con -> {
                            UserEntity ue = new UserEntity();
                            ue.setUsername(user.username());
                            ue.setFullname(user.fullname());
                            ue.setCurrency(user.currencyValues());
                            new UserdataDaoJdbc(con).create(ue);
                            return ue;
                        },
                        CFG.userdataJdbcUrl(), 1
                )
        );

        return UserJson.fromEntity(userEntity);
    }

    public UserJson findUserByUsername(UserJson user) {
        UserEntity ue = UserEntity.fromJson(user);
        Optional<UserEntity> findUser = transaction(connection -> Optional.ofNullable(new UserdataDaoJdbc(connection).create(ue)), CFG.userdataJdbcUrl());
        UserEntity userEntity = findUser.orElseThrow();

        return UserJson.fromEntity(userEntity);
    }

    public UserJson findById(UserJson user) {
        UserEntity ue = UserEntity.fromJson(user);
        Optional<UserEntity> findUser = transaction(
                connection -> new UserdataDaoJdbc(connection)
                        .findById(ue.getId())
                , CFG.userdataJdbcUrl()
        );
        UserEntity userEntity = findUser.orElseThrow();

        return UserJson.fromEntity(userEntity);
    }

    public AuthAutorityUserEntity create(AuthAutorityUserEntity authUser) {
        return transaction(
                (Function<Connection, AuthAutorityUserEntity>) connection ->
                        new AuthAuthorityUserDaoJdbc(connection).create(authUser),
                CFG.authJdbcUrl()
        );
    }

    public UserEntity create(UserEntity userEntity) {
        return transaction(
                (Function<Connection, UserEntity>) connection ->
                        new AuthUserDaoJdbc(connection).create(userEntity),
                CFG.userdataJdbcUrl()
        );
    }

    public void createUsersInAuthAuthororiryAndUserdataBases(AuthAutorityUserEntity aUser, UserEntity user) {
        Databases.XaConsumer createAuthorityUser = new Databases.XaConsumer(
                connection -> new AuthAuthorityUserDaoJdbc(connection).create(aUser)
                , CFG.authJdbcUrl(), 1
        );
        Databases.XaConsumer createUserEntity = new Databases.XaConsumer(
                connection -> new AuthUserDaoJdbc(connection).create(user)
                , CFG.userdataJdbcUrl(), 1
        );
        xaTransaction(createAuthorityUser, createUserEntity);
    }
}

