package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;


@Getter
public class RegistrationPage {
    //Элементы регистрации
    private final SelenideElement usernameField = $("#username");
    private final SelenideElement passwordField = $("#password");
    private final SelenideElement submitPasswordField = $("#passwordSubmit");
    private final SelenideElement singUpButton = $("#register-button");
    private final SelenideElement backToLoginPage = $(By.xpath("//a[@class=\"form__link\"]"));
    //Сообщения об ошибках
    private final SelenideElement errorMessageForUsernameFld = $(By.xpath("(//span[@class=\"form__error\"])[1]"));
    private final SelenideElement errorMessageForPasswordFld = $(By.xpath("(//span[@class=\"form__error\"])[2]"));
    //Сообщение о корректной авторизации
    private final SelenideElement messageAfterCorrectRegistration = $(By.xpath("//p[@class=\"form__paragraph form__paragraph_success\"]"));
    private final SelenideElement signInBtn = $(By.xpath("//a[@class=\"form_sign-in\"]"));

    public LoginPage registredNewUser(String userName, String password, String submitPassword) {
        usernameField.val(userName);
        passwordField.val(password);
        submitPasswordField.val(submitPassword);
        singUpButton.click();
        signInBtn.click();
        return new LoginPage();
    }

    public RegistrationPage incorrectRegistredNewUser(String userName, String password, String submitPassword) {
        usernameField.val(userName);
        passwordField.val(password);
        submitPasswordField.val(submitPassword);
        singUpButton.click();
        return new RegistrationPage();
    }

    public LoginPage backToLoginPageFromRegistrationPage() {
        backToLoginPage.click();
        return new LoginPage();
    }

    public RegistrationPage checkMessagePasswordsShouldBeEquals(String message) {
        errorMessageForPasswordFld.shouldHave(text(message));
        return this;
    }

}