package guru.qa.niffler.service.imp;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.AuthUserRepositorySpringJdbc;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.service.AuthClient;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.model.AuthUserJson.fromEntity;

public class AuthDbClient implements AuthClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserRepository authUserRepository = new AuthUserRepositoryJdbc();

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
    public AuthUserJson create(String username, String password) {
        return xaTransactionTemplate.execute(() -> {
            AuthUserEntity authUser = authUserEntity(username, password);
            return AuthUserJson.fromEntity(
                    authUserRepository.create(authUser)
            );
        });
    }

    @Override
    public AuthUserJson updateUser(AuthUserJson user) {
        AuthUserEntity auEn = AuthUserEntity.fromJson(user);

        AuthUserEntity auEnRes =  xaTransactionTemplate.execute(() ->
                authUserRepository.update(auEn)
        );
        return AuthUserJson.fromEntity(auEnRes);
    }

    @Override
    public AuthUserJson findById(UUID id) {
        Optional<AuthUserEntity> authUser = authUserRepository.findById(id);
        return fromEntity(authUser.get());
    }

    @Override
    public AuthUserJson findByUsername(String username) {
        Optional<AuthUserEntity> authUser = authUserRepository.findByUsername(username);
        AuthUserEntity aue = authUser.get();
        return fromEntity(aue);
    }

    @Override
    public void deleteUser(AuthUserJson user) {
        AuthUserEntity auEn = AuthUserEntity.fromJson(user);
        xaTransactionTemplate.execute(() -> {
                    authUserRepository.remove(auEn);
                    return null;
                }
        );
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
