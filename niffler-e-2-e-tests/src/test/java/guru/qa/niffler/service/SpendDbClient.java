package guru.qa.niffler.service;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.UUID;

public interface SpendDbClient {

    SpendJson createSpend(SpendJson spend);

    CategoryJson createCategory(CategoryJson category);

    SpendJson updateSpend(SpendJson spend);

    SpendJson findSpendById(UUID uuid);

    SpendJson findSpendByUsernameAndSpendDescription(String username, String description);

    CategoryJson findCategoryById(UUID id);

    CategoryJson findCategoryByUsernameAndSpendName(String username, String name);

    void remove(CategoryJson category);

    void remove(SpendJson spend);


}
