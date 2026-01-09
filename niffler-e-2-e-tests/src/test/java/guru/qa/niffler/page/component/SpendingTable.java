package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.EditSpendingPage;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class SpendingTable {
    private final SelenideElement self;
    private final SelenideElement period;
    private final SelenideElement editSpending;
    private final SelenideElement searchSpendinfFld;
    private final SelenideElement deleteBtn;

    EditSpendingPage editSpendingPage = new EditSpendingPage();


    public SpendingTable(SelenideElement self) {
        this.self = self;
        this.period = self.$("div[id='period']");
        this.editSpending = self.$("button[aria-label='Edit spending']");
        this.searchSpendinfFld = self.$("input[placeholder='Search']");
        this.deleteBtn = self.$("button[id='delete']");
    }

    public SpendingTable SelectPeriod(DataFilterValue value) {
        period.click();
        $("li[data-value='" + value.name() + "']")
                .click();
        SelenideElement periodListWithCheckPeriod = $("div[id=\"period\"]");

        switch (value) {
            case ALL:
                periodListWithCheckPeriod.shouldHave(exactText("All time"));
                break;
            case MONTH:
                periodListWithCheckPeriod.shouldHave(exactText("Last month"));
                break;
            case WEEK:
                periodListWithCheckPeriod.shouldHave(exactText("Last week"));
                break;
            case TODAY:
                periodListWithCheckPeriod.shouldHave(exactText("Today"));
                break;
            default:
                throw new IllegalArgumentException("Unknown period: " + value);
        }
        return this;
    }

    public EditSpendingPage editSpending(String decsription) {
        editSpending.click();
        editSpendingPage.setNewSpendingDescription(decsription);
        return null;
    }

    public SpendingTable deleteSpending(String description) {
        searchSpendinfFld.setValue(description);
        self.$$("span").findBy(text(description)).click();
        deleteBtn.click();
        return this;
    }

    public SpendingTable searchSpendingByDescription(String description) {
        searchSpendinfFld.setValue(description);
        ElementsCollection found = self.$$("span").filter(text(description));
        if (found.isEmpty()) {
            throw new AssertionError("Ничего не найдено");
        }
        return this;
    }

    public SpendingTable checkTableContainsDescriptions(String... expectedDescriptions) {
        ElementsCollection descriptions =
                self.$$("tbody tr td:nth-child(4) span");

        for (String expected : expectedDescriptions) {
            descriptions.findBy(text(expected))
                    .shouldBe(visible);
        }
        return this;
    }

    public SpendingTable checkTableSize(int expectedSize) {
        self.$$("tbody tr")
                .shouldHave(size(expectedSize));
        return this;
    }
}
