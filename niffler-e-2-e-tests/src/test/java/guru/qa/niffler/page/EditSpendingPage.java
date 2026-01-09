package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Calendar;

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
    public EditSpendingPage setAmount(String amount){
        amountFld.setValue(amount);
        return this;
    }

    @Nonnull
    public EditSpendingPage setCategory(String category){
        categoryFld.setValue(category);
        return this;
    }

    @Nonnull
    public EditSpendingPage setNewSpendingDescription(String description) {
        descriptionInput.clear();
        descriptionInput.setValue(description);
        return this;
    }

    @Nonnull
    public MainPage save() {
        saveBtn.click();
        return new MainPage();
    }

    @Nonnull
    public EditSpendingPage setDateInCalendar(Date date) {
        calendar.setDateInCalendar(date);
        return this;
    }

    @Nonnull
    public EditSpendingPage checkThatPageLoaded() {
        descriptionInput.shouldBe(visible);
        saveBtn.shouldBe(visible);
        return this;
    }
}
