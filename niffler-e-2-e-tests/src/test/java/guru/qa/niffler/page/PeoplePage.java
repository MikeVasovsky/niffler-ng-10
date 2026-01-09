package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class PeoplePage {

    private final SelenideElement peopleTab = $("a[href='/people/friends']");
    private final SelenideElement allTab = $("a[href='/people/all']");
    private final SelenideElement peopleTable = $("#all");

    public PeoplePage checkInvitationSentToUser(String username) {
        SelenideElement friendRow = peopleTable.$$("tr").find(text(username));
        friendRow.shouldHave(text("Waiting..."));
        return this;
    }

    public PeoplePage checkThatPageLoaded() {
        peopleTab.shouldBe(visible);
        allTab.shouldBe(visible);
        return this;
    }
}
