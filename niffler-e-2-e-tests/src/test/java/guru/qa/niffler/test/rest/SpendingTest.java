package guru.qa.niffler.test.rest;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendApiClient;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SpendingTest {

    public static final Config CFG = Config.getInstance();

    static SpendApiClient spendApiClient = new SpendApiClient();

    @User(
            spendings =
            @Spending(description = "test6_3", amount = 0.0, category = "")
    )
    @Test
    void testCreateSpend(UserJson user) {
        SpendJson createSpend = user.testData().spendings().getFirst();
        assertEquals(createSpend.username(), user.testData().spendings().getFirst().username());
    }

    @User(
            categories = @Category()
    )
    @Test
    void testCreateCategory(UserJson user) {
        CategoryJson createCategory = user.testData().categories().getFirst();
        assertEquals(createCategory.username(), user.testData().categories().getFirst().username());
    }

    @User(spendings = @Spending(
            description = "desc",
            amount = 99.00,
            currency = CurrencyValues.RUB,
            category = ""
    ))
    @Test
    void testGetSpend(UserJson user) {
        SpendJson spend = user.testData().spendings().getFirst();
        assertEquals(user.testData().spendings().getFirst().username(), spend.username());
    }

    @User(categories = @Category(name = "cat1"))
    @Test
    void testGetCategories(UserJson user) {
        List<CategoryJson> categories = spendApiClient.getCategories(user.testData().categories().getFirst().username());
        System.out.println(categories);
    }

    @User(spendings = {
            @Spending(
                    description = "desc1",
                    amount = 99.00,
                    currency = CurrencyValues.RUB,
                    category = "food"
            ),
            @Spending(
                    description = "desc2",
                    amount = 149.50,
                    currency = CurrencyValues.USD,
                    category = "transport"
            ),
            @Spending(
                    description = "desc3",
                    amount = 2000.00,
                    currency = CurrencyValues.RUB,
                    category = "entertainment"
            )
    })
    @Test
    void testGetAll(UserJson user) {
        List<SpendJson> allSpends = spendApiClient.allSpends(
                user.username(),
                null,
                null,
                null
        );
        System.out.println(allSpends);
    }

    @User(
            spendings =
            @Spending(description = "test6_3", amount = 2000.0, category = "")
    )
    @Test
    void testEditSpend(UserJson user) {
        SpendJson createSpend = user.testData().spendings().getFirst();
        SpendJson updateSpend = new SpendJson(
                createSpend.id(),
                new Date(),
                createSpend.category(),
                CurrencyValues.RUB,
                10000.00,
                "desc_6-3-1",
                user.username()
        );
        spendApiClient.editSpend(updateSpend);
        assertNotEquals(createSpend.amount(), updateSpend.amount());
        assertNotEquals(createSpend.description(), updateSpend.description());
    }

    @User(spendings =
    @Spending(description = "test6_3", amount = 0.0, category = ""))
    @Test
    void testRemoveSpend(UserJson user) {
        SpendJson createSpend = user.testData().spendings().getFirst();
        spendApiClient.removeSpends(createSpend.username(), createSpend.id().toString());
        try {
            spendApiClient.getSpend(createSpend.id().toString());
        } catch (AssertionError e) {
            System.out.println(e.getMessage());
            assertTrue(e.getMessage().contains("expected: <200> but was: <400>"));
        }
    }
}
