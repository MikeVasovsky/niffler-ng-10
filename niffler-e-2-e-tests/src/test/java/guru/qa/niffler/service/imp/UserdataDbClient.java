package guru.qa.niffler.service.imp;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.UserdataRepositorySpringJdbc;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserdataClient;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.model.UserJson.fromEntity;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

public class UserdataDbClient implements UserdataClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
    private final UserdataUserRepository userdataUserRepository = new UserdataRepositorySpringJdbc();
    private final String password = "12345";

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    DataSources.dataSource(CFG.authJdbcUrl())
            )
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    @Override
    public UserJson createUser(String username, String password) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = authUserEntity(username, password);
                    authUserRepository.create(authUser);
                    return fromEntity(
                            userdataUserRepository.create(userEntity(username)),
                            null
                    );
                }
        );
    }

    @Override
    public UserJson findById(UUID uuid) {
        Optional<UserEntity> userEntity = userdataUserRepository.findById(uuid);
        return fromEntity(userEntity.get(), null);
    }

    @Override
    public UserJson findByUsername(String username) {
        Optional<UserEntity> userEntity = userdataUserRepository.findByUsername(username);
        return fromEntity(userEntity.get(), null);
    }

    @Override
    public UserJson update(UserJson user) {
        UserEntity us = UserEntity.fromJson(user);
        UserEntity resEn =  xaTransactionTemplate.execute(() ->
                userdataUserRepository.update(us)
        );
        return UserJson.fromEntity(resEn,null);
    }

    @Override
    public void sendInvitation(UserJson targetUser, int count) {
        xaTransactionTemplate.execute(() -> {
            if (count > 0) {
                UserEntity targetEntity = userdataUserRepository.findById(
                        targetUser.id()
                ).orElseThrow();

                for (int i = 0; i < count; i++) {
                    String username = randomUsername();
                    AuthUserEntity authUser = authUserEntity(username, password);
                    authUserRepository.create(authUser);
                    UserEntity adressee = userdataUserRepository.create(userEntity(username));
                    userdataUserRepository.sendInvitation(targetEntity, adressee);
                }
            }
            return null;
        });
    }

    @Override
    public void remove(UserJson user) {
        UserEntity uEn = UserEntity.fromJson(user);
        xaTransactionTemplate.execute(()-> {
            userdataUserRepository.remove(uEn);
            return null;
        });
    }


    @Override
    public void addFriend(UserJson targetUser, int count) {
        xaTransactionTemplate.execute(() -> {
            if (count > 0) {
                UserEntity targetEntity = userdataUserRepository.findById(
                        targetUser.id()
                ).orElseThrow();

                for (int i = 0; i < count; i++) {
                    String username = randomUsername();
                    AuthUserEntity authUser = authUserEntity(username, password);
                    authUserRepository.create(authUser);
                    UserEntity adressee = userdataUserRepository.create(userEntity(username));
                    userdataUserRepository.addFriend(targetEntity, adressee);
                }
            }
            return null;
        });
    }


    private UserEntity userEntity(String username) {
        UserEntity ue = new UserEntity();
        ue.setUsername(username);
        ue.setCurrency(CurrencyValues.RUB);
        return ue;
    }

    private AuthUserEntity authUserEntity(String username, String password) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword(pe.encode(password));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(
                Arrays.stream(Authority.values()).map(
                        e -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setUser(authUser);
                            ae.setAuthority(e);
                            return ae;
                        }
                ).toList()
        );
        return authUser;
    }
}
