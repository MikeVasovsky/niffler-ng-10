package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthAutorityUserEntity;

public interface AuthAuthorityDao {
    AuthAutorityUserEntity create(AuthAutorityUserEntity authUser);
}
