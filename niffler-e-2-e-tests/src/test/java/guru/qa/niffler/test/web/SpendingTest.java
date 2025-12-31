package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

@WebTest
public class SpendingTest {

    private static final Config CFG = Config.getInstance();

    @User(
            username = "user_with_friend",
            categories = @Category(
                    archived = true
            ),
            spendings = @Spending(
                    category = "test 3-3",
                    amount = 89900,
                    currency = CurrencyValues.RUB,
                    description = "test3-3 descripton"
            )
    )
    @Test
    void spendingDescriptionShouldBeEditedByTableAction(SpendJson spending) {
        final String newDescription = "test3-3 descripton";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("user_with_friend", "111")
                .editSpending(spending.description())
                .setNewSpendingDescription(newDescription)
                .save()
                .checkThatTableContains(newDescription);
    }
}
