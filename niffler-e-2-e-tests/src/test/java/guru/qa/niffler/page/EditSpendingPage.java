package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Calendar;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.Date;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class EditSpendingPage {

    private final SelenideElement descriptionInput = $("#description");
    private final SelenideElement saveBtn = $("#save");
    private final SelenideElement amountFld = $("#amount");
    private final SelenideElement categoryFld = $("#category");

    private final Calendar calendar = new Calendar($("#root"));

    @Nonnull
    @Step("Вставить cумму")
    public EditSpendingPage setAmount(String amount){
        amountFld.setValue(amount);
        return this;
    }

    @Step("Вставить категорию")
    @Nonnull
    public EditSpendingPage setCategory(String category){
        categoryFld.setValue(category);
        return this;
    }

    @Step("Удалить старое описание и вставить новое")
    @Nonnull
    public EditSpendingPage setNewSpendingDescription(String description) {
        descriptionInput.clear();
        descriptionInput.setValue(description);
        return this;
    }

    @Step("охранить трату и перейти на главную страницу")
    @Nonnull
    public MainPage save() {
        saveBtn.click();
        return new MainPage();
    }

    @Step("Вставить дату в календарь")
    @Nonnull
    public EditSpendingPage setDateInCalendar(Date date) {
        calendar.setDateInCalendar(date);
        return this;
    }

    @Step("Проверить наличие полей на странице")
    @Nonnull
    public EditSpendingPage checkThatPageLoaded() {
        amountFld.shouldBe(visible);
        descriptionInput.shouldBe(visible);
        saveBtn.shouldBe(visible);
        return this;
    }
}
