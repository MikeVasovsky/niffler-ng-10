package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.UserExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegistrationPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UserExtension.class)
public class RegistrationTest {
  // Позитивные тесты
  private static final Config CFG = Config.getInstance();
  private final RegistrationPage registrationTest = new RegistrationPage();

  @User
  @Test
  public void checkCorrectRegistrationAndSignInSignIn(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class).
            goToRegistration();
    registrationTest.registredNewUser(user.username(), user.testData().password(), user.testData().password()).
            login(user.username(), user.testData().password())
            .checkThatPageLoaded();
  }

  @Test
  public void checkNavigateToLoginPageAfterClickToLinkInTheRegistrationPage() {
    Selenide.open(CFG.frontUrl(), LoginPage.class).
            goToRegistration().
            backToLoginPageFromRegistrationPage().
            checkThatPageLoad();
  }

  //Негативные тесты
  @Test
  public void checkMessageThenLogoPassIsShort() {
    Selenide.open(CFG.frontUrl(), LoginPage.class).
            goToRegistration();
    registrationTest.
            inputShortLogopass("mes","1","1","Allowed password length should be from 3 to 12 characters");

  }

  @Test
  public void checkMessageAfterIncorrectLogin() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .incorrectLogin("12", "22")
            .checkThatErrorMessageEqual("Неверные учетные данные пользователя");
  }

  @Test
  public void getTextAfterIncorrectPassAndConfirmPass() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .goToRegistration()
            .incorrectRegistredNewUser("user", "2222", "3333")
            .checkMessagePasswordsShouldBeEquals("Passwords should be equal");
  }
}