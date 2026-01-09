package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class FriendsPage {

    private final SelenideElement peopleTab = $("a[href='/people/friends']");
    private final SelenideElement allTab = $("a[href='/people/all']");
    private final SelenideElement requestsTable = $("#requests");
    private final SelenideElement friendsTable = $("#friends");
    private final SelenideElement acceptButton = $$("button").findBy(text("Accept"));
    private final SelenideElement declineButton = $$("button").findBy(text("Decline"));

    public FriendsPage acceptIncomeInv() {
        acceptButton.click();
        return this;
    }

    public FriendsPage declineIncomeInv() {
        declineButton.click();
        return this;
    }

    public FriendsPage checkExistingFriends(String... expectedUsernames) {
        friendsTable.$$("tr").shouldHave(textsInAnyOrder(expectedUsernames));
        return this;
    }

    public FriendsPage checkNoExistingFriends() {
        friendsTable.$$("tr").shouldHave(size(0));
        return this;
    }

    public FriendsPage checkExistingInvitations(String... expectedUsernames) {
        requestsTable.$$("tr").shouldHave(textsInAnyOrder(expectedUsernames));
        return this;
    }

    public void checkFriendsPageLoad() {
        allTab.shouldBe(visible);
        peopleTab.shouldBe(visible);
    }
}
