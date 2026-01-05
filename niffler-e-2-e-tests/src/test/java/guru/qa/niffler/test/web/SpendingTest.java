package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;

@WebTest
public class SpendingTest {

  private static final Config CFG = Config.getInstance();

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
  void testAddDateInCalendar(UserJson user){
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .successLogin(user.username(), user.testData().password())
            .goToSpendingPage()
            .setDateInCalendar(new Date(125, 2, 1));
  }
}
