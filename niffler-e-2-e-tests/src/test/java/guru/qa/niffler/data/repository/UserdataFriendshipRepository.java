package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.util.List;
import java.util.UUID;

public interface UserdataFriendshipRepository {

    List<FriendshipEntity> getAllFriendshipRequestById(UUID id);

}
