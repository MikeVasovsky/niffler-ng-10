package guru.qa.niffler.service.imp;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.SpenRepositorySpringJdbc;
import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.SpendRepositoryJdbc;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.model.CategoryJson.fromEntity;

public class SpendDbClient implements guru.qa.niffler.service.SpendDbClient {

    private static final Config CFG = Config.getInstance();

    private final SpendRepository spendDao = new SpendRepositoryJdbc();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    @Override
    public SpendJson createSpend(SpendJson spend) {
        return xaTransactionTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity category = spendDao.createCategory(spendEntity.getCategory());
                        spendEntity.setCategory(category);
                    }
                    return SpendJson.fromEntity(
                            spendDao.create(spendEntity)
                    );
                }
        );
    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        return xaTransactionTemplate.execute(() -> fromEntity(
                        spendDao.createCategory(
                                CategoryEntity.fromJson(category)
                        )
                )
        );
    }

    @Override
    public SpendJson updateSpend(SpendJson spend) {
        SpendEntity sp = SpendEntity.fromJson(spend);
        SpendEntity resultEn =  xaTransactionTemplate.execute(() ->
                spendDao.update(sp),null
        );
        return SpendJson.fromEntity(resultEn);
    }

    @Override
    public SpendJson findSpendById(UUID uuid) {
        Optional<SpendEntity> spendEntity = spendDao.findById(uuid);
        return SpendJson.fromEntity(spendEntity.get());
    }

    @Override
    public SpendJson findSpendByUsernameAndSpendDescription(String username, String description) {
        Optional<SpendEntity> spendEntity = spendDao.findByUsernameAndSpendDescription(username, description);
        return SpendJson.fromEntity(spendEntity.get());
    }

    @Override
    public CategoryJson findCategoryById(UUID id) {
        Optional<CategoryEntity> categoryEntity = spendDao.findCategoryById(id);
        return CategoryJson.fromEntity(categoryEntity.get());
    }

    @Override
    public CategoryJson findCategoryByUsernameAndSpendName(String username, String name) {
        Optional<CategoryEntity> spendEntity = spendDao.findCategoryByUsernameAndSpendName(username, name);
        return CategoryJson.fromEntity(spendEntity.get());
    }

    @Override
    public void remove(CategoryJson category) {
        CategoryEntity ct = CategoryEntity.fromJson(category);
        xaTransactionTemplate.execute(() -> {
            spendDao.removeCategory(ct);
            return null;
        });
    }

    @Override
    public void remove(SpendJson spend) {
        SpendEntity sp = SpendEntity.fromJson(spend);
        xaTransactionTemplate.execute(() -> {
            spendDao.remove(sp);
            return null;
        });
    }
}
