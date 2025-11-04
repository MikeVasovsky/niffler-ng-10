package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityUserDao;
import guru.qa.niffler.data.entity.auth.AuthAutorityUserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthAuthorityUserDaoJdbc implements AuthAuthorityUserDao {
    private final Connection connection;

    private static final Config CFG = Config.getInstance();


    public AuthAuthorityUserDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public AuthAutorityUserEntity create(AuthAutorityUserEntity authUser) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO user (username, password, enabled,account_non_expired, account_non_locked, credential_non_expired) "
                        + "?, ?, ?, ?, ?, ?")) {
            ps.setString(1, authUser.getUsername());
            ps.setString(2, authUser.getPassword());
            ps.setBoolean(3, authUser.getEnabled());
            ps.setBoolean(4, authUser.getAccountNonExpired());
            ps.setBoolean(5, authUser.getAccountNonLocked());
            ps.setBoolean(6, authUser.getCredentialsNonExpired());

            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            authUser.setId(generatedKey);
            return authUser;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
