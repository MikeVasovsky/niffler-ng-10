package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static guru.qa.niffler.model.CurrencyValues.RUB;
import static guru.qa.niffler.utils.RandomDataUtils.randomSentence;
import static java.lang.String.valueOf;

@WebTest
public class SpendingTest {

    private static final Config CFG = Config.getInstance();

    @User
    @Test
    void addSpend(UserJson user) {
        String description = RandomDataUtils.randomName();
        String category = RandomDataUtils.randomName();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .goToSpendingPage()
                .setAmount("1999")
                .setNewSpendingDescription(description)
                .setCategory(category)
                .save()
                .searchSpendingByDescription(description);
    }

    @User(
            spendings = @Spending(
                    amount = 89990.00,
                    description = "Обучение Niffler 2.0 юбилейный поток!",
                    category = "Обучение"
            )
    )
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin(UserJson user) {
        final String spendDescription = user.testData().spendings().getFirst().description();
        final String newDescription = "Обучение Niffler Next Generation";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .editSpending(spendDescription)
                .setNewSpendingDescription(newDescription)
                .save()
                .checkThatTableContainsSpending(newDescription);
    }

    @User(
            spendings = @Spending(
                    amount = 89990.00,
                    description = "Обучение Niffler 2.0 юбилейный поток!",
                    category = "Обучение"
            )
    )
    @Test
    void AddDateInCalendar(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .goToSpendingPage()
                .setDateInCalendar(new Date(125, 2, 1));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ALL",
            "MONTH",
            "WEEK",
            "TODAY"
    })
    void getSpendingsByPeriod(String value) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin("krylovis", "1111")
                .selectSpendingPeriod(value);
    }

    @User(
            spendings = @Spending(
                    description = "deck",
                    amount = 1000.00,
                    currency = RUB,
                    category = "someCat"
            )
    )
    @Test
    void deleteSpending(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .deleteSpendingByDescription(user.testData().spendings().getFirst().description());
    }

    @User(
            spendings = @Spending(
                    description = "deck",
                    amount = 1000.00,
                    currency = RUB,
                    category = "someCat"
            )
    )
    @Test
    void setDescription(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .editSpending(randomSentence(1));
    }

    @User(
            spendings = @Spending(
                    description = "deck",
                    amount = 1000.00,
                    currency = RUB,
                    category = "someCat"
            )
    )
    @Test
    void findSpendingByDescription(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .searchSpendingByDescription(user.testData().spendings().getFirst().description());
    }

    @ParameterizedTest
    @MethodSource("spendingDescriptions")
    void shouldContainsAllSpendings(List<String> descriptions) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin("krylovis", "1111")
                .checkSpendingTableContains(
                        descriptions.toArray(String[]::new)
                );
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void sizeOfSpendingTableShouldEqualsToArgument(int size) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin("krylovis", "1111")
                .checkSpendingsBySize(size);
    }

    static Stream<List<String>> spendingDescriptions() {
        return Stream.of(
                List.of("ТЕСТ", "1"),
                List.of("11"),
                List.of("ТЕСТ", "11", "1")
        );
    }

}
