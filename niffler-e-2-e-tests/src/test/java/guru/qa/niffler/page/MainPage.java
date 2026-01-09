package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.DataFilterValue;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SearchField;
import guru.qa.niffler.page.component.SpendingTable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class MainPage {

    private final SelenideElement header = $("#root header");
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement statComponent = $("#stat");
    private final SelenideElement spendingTable = $("#spendings");
    private final SelenideElement searchTab = $("#root form input");
    private final SelenideElement newSpendBtn = $("#root a[tabindex='0']");

    private final SearchField searchField = new SearchField(searchTab);
    private final SpendingTable spTab = new SpendingTable(spendingTable);
    private final Header hdr = new Header(header);

    public FriendsPage goToFriendsPage() {
        return hdr.toFriendsPage();
    }

    public PeoplePage goToallPeoplesPage() {
        return hdr.toAllPeoplePage();
    }

    public ProfilePage goToProfilePage() {
        return hdr.toProfilePage();
    }

    public EditSpendingPage goToEditSpendingPage() {
        return hdr.addSpendingPage();
    }

    public LoginPage logOut() {
        return hdr.signOut();

    }

    public EditSpendingPage editSpending(String spendingDescription) {
        spTab.editSpending(spendingDescription);
        return new EditSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

    public MainPage checkThatPageLoaded() {
        statComponent.shouldHave(text("Statistics"));
        spendingTable.shouldHave(text("History of Spendings"));
        return this;
    }

    public MainPage checkSearch(String query) {
        searchField.search(query);
        tableRows.findBy(text(query)).shouldBe(visible);
        return this;
    }

    public MainPage inputTextInSearchField(String val) {
        searchField.inputText(val);
        return this;
    }

    public MainPage checkClearSearchFieldIfIsNotEmpty() {
        searchField.clearIfNotEmpty();
        return this;
    }

    public EditSpendingPage goToSpendingPage() {
        newSpendBtn.click();
        return new EditSpendingPage();
    }

    public MainPage selectSpendingPeriod(String value) {
        spTab.SelectPeriod(DataFilterValue.valueOf(value));
        return this;
    }

    public MainPage deleteSpendingByDescription(String value) {
        spTab.deleteSpending(value);
        return this;
    }

    public MainPage searchSpendingByDescription(String value) {
        spTab.searchSpendingByDescription(value);
        return this;
    }

    public MainPage checkSpendingTableContains(String... descriptions) {
        spTab.checkTableContainsDescriptions(descriptions);
        return this;
    }

    public MainPage checkSpendingsBySize(int size) {
        spTab.checkTableSize(size);
        return this;
    }
}
