package guru.qa.niffler.service.imp;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;

import guru.qa.niffler.data.repository.impl.SpendRepositoryJdbc;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.model.CategoryJson.fromEntity;

public class SpendDbClient implements SpendClient {

    private static final Config CFG = Config.getInstance();

    private final SpendRepository spendRep = new SpendRepositoryJdbc();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    @Override
    public SpendJson createSpend(SpendJson spend) {
        return xaTransactionTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity category = spendRep.createCategory(spendEntity.getCategory());
                        spendEntity.setCategory(category);
                    }
                    return SpendJson.fromEntity(
                            spendRep.create(spendEntity)
                    );
                }
        );
    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        return xaTransactionTemplate.execute(() -> fromEntity(
                spendRep.createCategory(
                                CategoryEntity.fromJson(category)
                        )
                )
        );
    }

    @Override
    public CategoryJson updateCategory(CategoryJson category) {
        CategoryEntity sp = CategoryEntity.fromJson(category);
        CategoryEntity resultEn =  xaTransactionTemplate.execute(() ->
                spendRep.update(sp)
        );
        return CategoryJson.fromEntity(resultEn);
    }

    @Override
    public SpendJson editSpend(SpendJson spend) {
        SpendEntity sp = SpendEntity.fromJson(spend);
        SpendEntity resultEn =  xaTransactionTemplate.execute(() ->
                spendRep.update(sp)
        );
        return SpendJson.fromEntity(resultEn);
    }

    @Override
    public SpendJson getSpend(String id) {
        Optional<SpendEntity> spendEntity = spendRep.findById(UUID.fromString(id));
        return SpendJson.fromEntity(spendEntity.get());
    }

    @Override
    public List<SpendJson> allSpends(String username, @Nullable CurrencyValues currency, @Nullable Date from, @Nullable Date to) {
        throw new UnsupportedOperationException("Данный метод не реализован");
    }

    @Override
    public void removeSpends(String username, String... ids) {
        xaTransactionTemplate.execute(
                () -> {
                    for (String id : ids) {
                        Optional<SpendEntity> spend = spendRep.findById(UUID.fromString(id));
                        spend.ifPresent(spendRep::remove);
                    }
                    return null;
                }
        );
    }


    @Override
    public SpendJson findSpendByUsernameAndSpendDescription(String username, String description) {
        Optional<SpendEntity> spendEntity = spendRep.findByUsernameAndSpendDescription(username, description);
        return SpendJson.fromEntity(spendEntity.get());
    }

    public CategoryJson findCategoryById(UUID id) {
        Optional<CategoryEntity> categoryEntity = spendRep.findCategoryById(id);
        return CategoryJson.fromEntity(categoryEntity.get());
    }

    public CategoryJson findCategoryByUsernameAndSpendName(String username, String name) {
        Optional<CategoryEntity> spendEntity = spendRep.findCategoryByUsernameAndCategoryName(username, name);
        return CategoryJson.fromEntity(spendEntity.get());
    }

    @Override
    public void removeCategory(CategoryJson category) {
        CategoryEntity ct = CategoryEntity.fromJson(category);
        xaTransactionTemplate.execute(() -> {
            spendRep.removeCategory(ct);
            return null;
        });
    }

    @Override
    public List<CategoryJson> allCategories(String username) {
       List<CategoryJson> res =  xaTransactionTemplate.execute(() ->
                spendRep.allCategories(username)
                        .stream()
                        .map(CategoryJson::fromEntity).toList()

        );
       return res;
    }
}
