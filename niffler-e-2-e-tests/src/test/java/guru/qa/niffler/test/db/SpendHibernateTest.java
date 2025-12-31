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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.UUID;

import static java.lang.String.valueOf;
import static java.util.Arrays.stream;
import static java.util.UUID.fromString;
import static org.junit.jupiter.api.Assertions.*;

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
                                "someTest11",
                                "update_user_with_friend",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "someTest11",
                        "update_user_with_friend"
                )
        );
        assertNotNull(spend);
        assertEquals("update_user_with_friend", spend.username());
    }

    @Test
    void createCategory() {
        CategoryJson categoryJson = spendDbClient.createCategory(
                new CategoryJson(
                        null,
                        "создание категории",
                        "mikeVasovsky",
                        false
                )
        );
        assertNotNull(categoryJson);
        assertEquals("mikeVasovsky", categoryJson.username());
    }

    @Test
    void updateSpend() {
        SpendEntity spend = SpendEntity.fromJson(new SpendJson(
                UUID.fromString("55cd72ee-dcd9-11f0-bf48-0242ac110002"),
                new Date(),
                new CategoryJson(
                        UUID.fromString("55cad6c4-dcd9-11f0-bf48-0242ac110002"),
                        "update_user_with_friend_hahaha11",
                        "update_user_with_friend_update",
                        true),
                CurrencyValues.RUB,
                12332.0,
                "update_user_with_friend_hahaha-name-tx-311",
                "update_user_with_friend_update"

        ));
        SpendJson oldSpend = spendDbClient.getSpend(valueOf(spend.getId()));
        SpendJson newSpend = spendDbClient.editSpend(SpendJson.fromEntity(spend));
        assertNotEquals(newSpend, oldSpend);
    }

    @Test
    void findSpendByUsernameAndSpendName() {
        String username = "update_user_with_friend";
        String description = "someTest";
        SpendJson spend = spendDbClient.findSpendByUsernameAndSpendDescription(username, description);
        assertEquals("update_user_with_friend", spend.username());
        assertEquals("someTest", spend.description());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "19d875f2-de93-11f0-b4a6-0242ac110002",
            "b2ba7fd2-bed5-11f0-a56a-6e4e4369ca5b"
    })
    void fineSpendById(UUID uuid) {
        SpendJson spendJson = spendDbClient
                .getSpend(valueOf(uuid));
        assertEquals(spendJson.id(), uuid);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "19d6a9ac-de93-11f0-b4a6-0242ac110002",
            "b2b87642-bed5-11f0-a56a-6e4e4369ca5b"
    })
    void fineCategoryById(UUID uuid) {
        CategoryJson categoryJson = spendDbClient
                .findCategoryById(uuid);
        assertEquals(categoryJson.id(), uuid);
    }

    @Test
    void findCategoryByUsernameAndSpendName() {
        String username = "user_with_friend";
        String spendName = "testCreate";
        CategoryJson spend = spendDbClient.findCategoryByUsernameAndSpendName(username, spendName);
        assertEquals("user_with_friend", spend.username());
        assertEquals("someTest", spend.name());
    }

    @Test
    void deleteCategory() {
        CategoryEntity category = CategoryEntity.fromJson(
                new CategoryJson(
                        fromString("d38fbecc-dc06-11f0-826b-0242ac110002"),
                        "Обучение!",
                        "user_with_friend",
                        false
                )
        );
        spendDbClient.removeCategory(CategoryJson.fromEntity(category));
        try {
            spendDbClient.getSpend(valueOf(category.getId()));
        } catch (NoSuchElementException o) {
            assertEquals(o.toString(), "java.util.NoSuchElementException: No value present");
        }
    }

    @Test
    void deleteSpend() {
        SpendEntity spend = SpendEntity.fromJson(new SpendJson(
                UUID.fromString("19d875f2-de93-11f0-b4a6-0242ac110002"),
                new Date(),
                new CategoryJson(
                        UUID.fromString("d38fbecc-dc06-11f0-826b-0242ac110002"),
                        "testCreate",
                        "user_with_friend",
                        false),
                CurrencyValues.RUB,
                12332.0,
                "update_spend-name-tx-3",
                "update_user_with_friend__test"

        ));

        SpendJson delSpend = SpendJson.fromEntity(spend);
        spendDbClient.removeSpends(delSpend.username(), valueOf(delSpend.id()));
        try {
            spendDbClient.getSpend(valueOf(spend.getId()));
        } catch (NoSuchElementException o) {
            assertEquals(o.toString(), "java.util.NoSuchElementException: No value present");
        }
    }
}
