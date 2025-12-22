package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.FriendshipStatus;
import guru.qa.niffler.model.UserJson;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

import static guru.qa.niffler.data.tpl.DataSources.dataSource;

public class UsersDbClient implements UsersClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserDao authUserDao = new AuthUserDaoJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoJdbc();
    private final UserdataUserDao udUserDao = new UserdataUserDaoSpringJdbc();

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    dataSource(CFG.authJdbcUrl())
            )
    );

    //Использование ChainedTransactionManager
    private final TransactionTemplate chainedTxTemplate = new TransactionTemplate(new ChainedTransactionManager(
            new JdbcTransactionManager(
                    dataSource(CFG.authJdbcUrl())
            ),
            new JdbcTransactionManager(
                    dataSource(CFG.userdataJdbcUrl())
            )
    ));

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    private final JdbcTransactionTemplate jdbcTransactionTemplate = new JdbcTransactionTemplate(
            CFG.authJdbcUrl()
    );

    //Распределенная транзакция, через ChainedTransactionManager
    public UserJson createUserChained(UserJson user) {
        return chainedTxTemplate.execute(status -> {
                    try {
                        AuthUserEntity authUser = new AuthUserEntity();
                        authUser.setUsername(user.username());
                        authUser.setPassword(pe.encode("12345"));
                        authUser.setEnabled(true);
                        authUser.setAccountNonExpired(true);
                        authUser.setAccountNonLocked(true);
                        authUser.setCredentialsNonExpired(true);

                        AuthUserEntity createdAuthUser = authUserDao.create(authUser);

                        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                                e -> {
                                    AuthorityEntity ae = new AuthorityEntity();
                                    ae.setUserId(null);
                                    ae.setAuthority(e);
                                    return ae;
                                }
                        ).toArray(AuthorityEntity[]::new);

                        authAuthorityDao.create(authorityEntities);
                        return UserJson.fromEntity(
                                udUserDao.create(UserEntity.fromJson(user)),
                                null
                        );
                    } catch (Exception e) {
                        status.setRollbackOnly();
                        throw e;
                    }
                }
        );
    }

    @Override
    public UserJson createUser(UserJson user) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword(pe.encode("12345"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);

                    AuthUserEntity createdAuthUser = authUserDao.create(authUser);

                    AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUserId(createdAuthUser.getId());
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toArray(AuthorityEntity[]::new);

                    authAuthorityDao.create(authorityEntities);
                    return UserJson.fromEntity(
                            udUserDao.create(UserEntity.fromJson(user)),
                            null
                    );
                }
        );
    }

    //Создание юзера в таблице niffler-userdata.user
    public UserJson createUserdataUser(UserJson user) {
        return jdbcTransactionTemplate.execute(() -> {
                    UserEntity authUser = new UserEntity();
                    authUser.setUsername(user.username());
                    authUser.setCurrency(user.currency());
                    authUser.setFirstname(user.firstname());
                    authUser.setSurname(user.surname());
                    UserEntity createdUser = udUserDao.create(authUser);
                    return UserJson.fromEntity(createdUser, FriendshipStatus.FRIEND
                    );
                }
        );
    }
}
