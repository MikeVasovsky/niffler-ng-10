package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.model.CurrencyValues.RUB;

public class MainTest {
    private final Config CFG = Config.getInstance();


    @User(
            spendings = @Spending(
                description = "testDesc",
                amount = 1000.00,
                currency = RUB,
                category = "testCat"
        )

    )
    @Test
    void testSearch(UserJson user){
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .checkSearch(user.testData().spendings().getFirst().category().name());
    }

    @User(
            spendings = @Spending(
                    description = "testDesc",
                    amount = 1000.00,
                    currency = RUB,
                    category = "testCat"
            )

    )
    @Test
    void testClearSearchFieldIfIsNotEmpty(UserJson user){
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .inputTextInSearchField(user.testData().spendings().getFirst().username())
                .checkClearSearchFieldIfIsNotEmpty();
    }

}
