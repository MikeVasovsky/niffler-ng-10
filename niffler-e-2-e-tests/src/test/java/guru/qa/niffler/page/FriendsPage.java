package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

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

    @Step("Принять предложение дружбы")
    public FriendsPage acceptIncomeInv() {
        acceptButton.click();
        return this;
    }

    @Step("Отклонить предложение дружбы")
    public FriendsPage declineIncomeInv() {
        declineButton.click();
        return this;
    }

    @Step("Проверить наличие друзей на странице")
    public FriendsPage checkExistingFriends(String... expectedUsernames) {
        friendsTable.$$("tr").shouldHave(textsInAnyOrder(expectedUsernames));
        return this;
    }

    @Step("Проверить отсутствие друзей на странице")
    public FriendsPage checkNoExistingFriends() {
        friendsTable.$$("tr").shouldHave(size(0));
        return this;
    }

    @Step("Проверить входящие предложения дружбы")
    public FriendsPage checkExistingInvitations(String... expectedUsernames) {
        requestsTable.$$("tr").shouldHave(textsInAnyOrder(expectedUsernames));
        return this;
    }

    @Step("Проверить наличие полей на странице")
    public void checkFriendsPageLoad() {
        friendsTable.shouldBe(visible);
        requestsTable.shouldBe(visible);
        peopleTab.shouldBe(visible);
        allTab.shouldBe(visible);
    }
}
