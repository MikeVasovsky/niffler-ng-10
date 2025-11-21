package guru.qa.niffler.data.mapper.springmapper;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthAuthorityResultSetExtractor implements ResultSetExtractor<AuthUserEntity> {

    public final static AuthAuthorityResultSetExtractor instance = new AuthAuthorityResultSetExtractor();

    private AuthAuthorityResultSetExtractor() {}

    @Override
    public AuthUserEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<UUID, AuthUserEntity> userMap = new ConcurrentHashMap<>();
        UUID userId = null;
        while (rs.next()) {
            userId = rs.getObject("id", UUID.class);
            AuthUserEntity user = userMap.computeIfAbsent(userId, id -> {
                AuthUserEntity userEntity = new AuthUserEntity();
                try {
                    userEntity.setId(id);
                    userEntity.setUsername(rs.getString("username"));
                    userEntity.setPassword(rs.getString("password"));
                    userEntity.setEnabled(rs.getBoolean("enabled"));
                    userEntity.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                    userEntity.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                    userEntity.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                    userEntity.setAuthorities(new ArrayList<>());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return userEntity;
            });
            AuthorityEntity authoririty = new AuthorityEntity();
            authoririty.setId(rs.getObject("authority_id", UUID.class));
            authoririty.setAuthority(Authority.valueOf(rs.getString("authority")));
            authoririty.setUser(user);
            user.addAuthorities(authoririty);
        }
        return userMap.get(userId);
    }
}
