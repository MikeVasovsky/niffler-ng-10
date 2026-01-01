package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.By.xpath;

public class PeoplePage {
    private final SelenideElement peopleTab = $("a[href='/people/friends']");
    private final SelenideElement allTab = $("a[href='/people/all']");
    private final SelenideElement peopleTable = $("#all");
    private final SelenideElement peopleSearchField = $(xpath("//input[@placeholder=\"Search\"]"));

    public PeoplePage checkInvitationSentToUser(String username) {
        SelenideElement friendRow = peopleTable.$$("tr").find(text(username));
        friendRow.shouldHave(text("Waiting..."));
        return this;
    }

    public PeoplePage searchPeople(String name) {
        peopleSearchField.val(name)
                .pressEnter();
        return this;
    }

    public PeoplePage checkThatTableContains(String name) {
        peopleTable.$$("tbody tr").find(text(name)).should(visible);
        return this;
    }
}