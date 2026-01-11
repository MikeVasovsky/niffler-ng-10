package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.DataFilterValue;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SearchField;
import guru.qa.niffler.page.component.SpendingTable;
import io.qameta.allure.Step;

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

    @Step("Перейти в раздел 'Friends'")
    public FriendsPage goToFriendsPage() {
        return hdr.toFriendsPage();
    }

    @Step("Перейти в раздел 'All people'")
    public PeoplePage goToallPeoplesPage() {
        return hdr.toAllPeoplePage();
    }

    @Step("Перейти в раздел 'Profile'")
    public ProfilePage goToProfilePage() {
        return hdr.toProfilePage();
    }

    @Step("Перейти в раздел 'Add new spending'")
    public EditSpendingPage goToEditSpendingPage() {
        return hdr.addSpendingPage();
    }

    @Step("Разлогиниться")
    public LoginPage logOut() {
        return hdr.signOut();
    }

    @Step("Перейти в раздел 'Edit spending'")
    public EditSpendingPage editSpending(String spendingDescription) {
        spTab.editSpending(spendingDescription);
        return new EditSpendingPage();
    }

    @Step("Проверить, наличие плат на странице")
    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

    @Step("Проверить наличие полей на странице")
    public MainPage checkThatPageLoaded() {
        statComponent.shouldHave(text("Statistics"));
        spendingTable.shouldHave(text("History of Spendings"));
        return this;
    }

    @Step("Поиск траты")
    public MainPage search(String query) {
        searchField.search(query);
        tableRows.findBy(text(query)).shouldBe(visible);
        return this;
    }

    @Step("Вставить текст в поле поиска")
    public MainPage inputTextInSearchField(String val) {
        searchField.inputText(val);
        return this;
    }

    @Step("Удалить текст из поля поиска, если оно не пустое")
    public MainPage checkClearSearchFieldIfIsNotEmpty() {
        searchField.clearIfNotEmpty();
        return this;
    }

    @Step("Выбрать период траты")
    public MainPage selectSpendingPeriod(String value) {
        spTab.SelectPeriod(DataFilterValue.valueOf(value));
        return this;
    }

    @Step("Удалить трату по описанию")
    public MainPage deleteSpendingByDescription(String value) {
        spTab.deleteSpending(value);
        return this;
    }

    @Step("ПОиск траты по описанию")
    public MainPage searchSpendingByDescription(String value) {
        spTab.searchSpendingByDescription(value);
        return this;
    }

    @Step("Проверить, что на странице есть траты с данным описанием")
    public MainPage checkSpendingTableContains(String... descriptions) {
        spTab.checkTableContainsDescriptions(descriptions);
        return this;
    }

    @Step("Проверить, что на странице данное количество трат")
    public MainPage checkSpendingsBySize(int size) {
        spTab.checkTableSize(size);
        return this;
    }
}
