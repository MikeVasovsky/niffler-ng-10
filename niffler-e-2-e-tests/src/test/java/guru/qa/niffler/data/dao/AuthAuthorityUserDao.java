package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthAutorityUserEntity;

public interface AuthAuthorityUserDao {
    AuthAutorityUserEntity create(AuthAutorityUserEntity authUser);
}
