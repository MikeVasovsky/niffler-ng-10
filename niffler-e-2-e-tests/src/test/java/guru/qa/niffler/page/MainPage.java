package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.By.xpath;

public class MainPage {
  private final SelenideElement spendingTable = $("#spendings");
  private final SelenideElement spendSearchField = $(xpath("//input[@placeholder=\"Search\"]"));
  private final SelenideElement profileMenuBtn = $(xpath("//div[@class=\"MuiAvatar-root MuiAvatar-circular MuiAvatar-colorDefault css-1pqo26w\"]"));
  private final SelenideElement profileBtn = $(xpath("//a[contains(text(), 'Profile')]"));
  private final SelenideElement friendsBtn = $(xpath("//a[@href=\"/people/friends\"]"));
  private final SelenideElement allPeoplrBtn = $(xpath("//a[@href=\"/people/all\"]"));

  public MainPage checkThatPageLoaded() {
    spendingTable.should(visible);
    return this;
  }

  public EditSpendingPage editSpending(String description) {
    spendingTable.$$("tbody tr").find(text(description)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  public MainPage checkThatTableContains(String spend) {
    spendingTable.$$("tbody tr").find(text(spend)).should(visible);
    return this;
  }

  public ProfilePage goToProfilePage() {
    profileMenuBtn.click();
    profileBtn.click();
    return new ProfilePage();
  }

  public FriendsPage goToFriendsPage() {
    profileMenuBtn.click();
    friendsBtn.click();
    return new FriendsPage();
  }

  public PeoplePage goToPeoplePage() {
    profileMenuBtn.click();
    allPeoplrBtn.click();
    return new PeoplePage();
  }


  public MainPage searchSpending(String spendName){
    spendSearchField.val(spendName)
            .pressEnter();
    return this;
  }
}