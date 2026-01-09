package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.*;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class Header {

    private final SelenideElement self;
    private final SelenideElement menuBtn;

    public Header(SelenideElement self) {
        this.self = self;
        this.menuBtn = self.$("[aria-label='Menu']");
    }

    public FriendsPage toFriendsPage() {
        menuBtn.click();
        $("#account-menu")
                .$$("li[role='menuitem']")
                .findBy(text("Friends"))
                .click();
        return new FriendsPage();
    }

    public PeoplePage toAllPeoplePage() {
        menuBtn.click();
        $("#account-menu")
                .$$("li[role='menuitem']")
                .findBy(text("All People"))
                .click();
        return new PeoplePage();
    }

    public ProfilePage toProfilePage() {
        menuBtn.click();
        $("#account-menu")
                .$$("li[role='menuitem']")
                .findBy(text("Profile"))
                .click();
        return new ProfilePage();
    }

    public LoginPage signOut() {
        menuBtn.click();
        $("#account-menu")
                .$$("li[role='menuitem']")
                .findBy(text("Sign out"))
                .click();
        $$("button")
                .findBy(text("Log out"))
                .click();
        return new LoginPage();
    }

    public EditSpendingPage addSpendingPage() {
        self.$("#root a[href=\"/spending\"]")
                .click();
        return new EditSpendingPage();
    }

    public MainPage toMainPage() {
        self.$("h1").click();
        return new MainPage();
    }
}
