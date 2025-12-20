package guru.qa.niffler.test.db;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.imp.SpendDbClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;
import java.util.UUID;

import static java.util.UUID.fromString;

public class SpendHibernateTest {

    SpendDbClient spendDbClient = new SpendDbClient();

    @Test
    void createSpend() {
        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "someTest",
                                "update_user_with_friend",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "someTest",
                        "update_user_with_friend"
                )
        );

        System.out.println(spend);
    }

    @Test
    void createCategory() {
        spendDbClient.createCategory(
                new CategoryJson(
                        null,
                        "создание категории",
                        "mikeVasovsky",
                        false
                )
        );
    }

    @Test
    void updateSpend() {
        SpendEntity spend = SpendEntity.fromJson(new SpendJson(
                UUID.fromString("d3912906-dc06-11f0-826b-0242ac110002"),
                new Date(),
                new CategoryJson(
                        UUID.fromString("55cad6c4-dcd9-11f0-bf48-0242ac110002"),
                        "updateCategory",
                        "user_with_friend_create",
                        true),
                CurrencyValues.RUB,
                12332.0,
                "update_spend-name-tx-3",
                "update_user_with_friend__test"

        ));
        spendDbClient.updateSpend(spend);
    }

    @Test
    void findSpendByUsernameAndSpendName() {
        String username = "update_user_with_friend";
        String description = "update_spend-name-tx-3";
        SpendJson spend = spendDbClient.findSpendByUsernameAndSpendDescription(username, description);
        System.out.println(spend);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "d3912906-dc06-11f0-826b-0242ac110002"
    })
    void fineSpendById(UUID uuid) {
        SpendJson spendJson = spendDbClient
                .findSpendById(uuid);
        System.out.println(spendJson);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "d38fbecc-dc06-11f0-826b-0242ac110002"
    })
    void fineCategoryById(UUID uuid) {
        CategoryJson spendJson = spendDbClient
                .findCategoryById(uuid);
        System.out.println(spendJson);
    }

    @Test
    void findCategoryByUsernameAndSpendName() {
        String username = "user_with_friend";
        String spendName = "testCreate";
        CategoryJson spend = spendDbClient.findCategoryByUsernameAndSpendName(username, spendName);
        System.out.println(spend);
    }

    @Test
    void deleteCategory() {
        CategoryEntity category = CategoryEntity.fromJson(
                new CategoryJson(
                        fromString("a754ad9c-adbf-11f0-98fe-0242ac110002"),
                        "Обучение!",
                        "mikeVasovsky",
                        false
                )
        );
        spendDbClient.remove(category);
    }

    @Test
    void deleteSpend() {
        SpendEntity spend = SpendEntity.fromJson(new SpendJson(
                UUID.fromString("d38fbecc-dc06-11f0-826b-0242ac110002"),
                new Date(),
                new CategoryJson(
                        UUID.fromString("d38fbecc-dc06-11f0-826b-0242ac110002"),
                        "testCreate",
                        "user_with_friend",
                        false),
                CurrencyValues.RUB,
                12332.0,
                "update_spend-name-tx-3",
                "update_user_with_friend"

        ));
        spendDbClient.remove(spend);
    }
}
