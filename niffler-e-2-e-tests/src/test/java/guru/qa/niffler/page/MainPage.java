package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
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
  private final SelenideElement headerMenu = $("ul[role='menu']");
  private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
  private final SelenideElement statComponent = $("#stat");
  private final SelenideElement spendingTable = $("#spendings");
  private final SelenideElement searchTab = $("#root form input");
  private final SelenideElement newSpendBtn = $("#root a[tabindex='0']");

  private final SearchField searchField = new SearchField(searchTab);
  private final SpendingTable spTab = new SpendingTable(spendingTable);

  @Nonnull
  public FriendsPage friendsPage() {
    header.$("button").click();
    headerMenu.$$("li").find(text("Friends")).click();
    return new FriendsPage();
  }

  @Nonnull
  public PeoplePage allPeoplesPage() {
    header.$("button").click();
    headerMenu.$$("li").find(text("All People")).click();
    return new PeoplePage();
  }

  @Nonnull
  public EditSpendingPage editSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  public void checkThatTableContainsSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).should(visible);
  }

  @Nonnull
  public MainPage checkThatPageLoaded() {
    statComponent.should(visible).shouldHave(text("Statistics"));
    spendingTable.should(visible).shouldHave(text("History of Spendings"));
    return this;
  }

  @Nonnull
  public MainPage checkSearch(String query){
    searchField.search(query);
    tableRows.findBy(text(query)).shouldBe(visible);
    return this;
  }

  @Nonnull
  public MainPage inputTextInSearchField(String val){
    searchField.inputText(val);
    return this;
  }

  @Nonnull
  public MainPage checkClearSearchFieldIfIsNotEmpty(){
    searchField.clearIfNotEmpty();
    return this;
  }

  @Nonnull
  public EditSpendingPage goToSpendingPage(){
    newSpendBtn.click();
    return new EditSpendingPage();
  }
}
