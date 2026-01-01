package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
public class SpendingTest {

    private static final Config CFG = Config.getInstance();

    @User(
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
    void spendingDescriptionShouldBeEditedByTableAction(UserJson user) {
        final String newDescription = "test 6.3 descripton";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .editSpending(user.testData().categories().getFirst().name())
                .setNewSpendingDescription(newDescription)
                .save()
                .checkThatTableContains(newDescription);
    }

    @Test
    void findSpendBySearhField() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("mikeVasovsky", "1111")
                .checkThatPageLoaded()
                .searchSpending("new_63_cat")
                .checkThatTableContains("new_63_cat");
    }
}