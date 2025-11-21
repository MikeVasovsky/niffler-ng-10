package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.List;
import java.util.UUID;

public interface SpendRepository {
    SpendEntity create(SpendEntity spend);

    List<SpendEntity> findSpendsAndCategoriesById(UUID categoryId);

}
