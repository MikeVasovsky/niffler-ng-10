package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import javax.annotation.ParametersAreNonnullByDefault;

import static java.lang.String.valueOf;

@WebTest
public class FriendsTest {

    private static final Config CFG = Config.getInstance();

    @User(
            incomeInvitations = 1
    )
    @Test
    void acceptIncomeInvitation(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .goToFriendsPage()
                .acceptIncomeInv();
    }

    @User(
            incomeInvitations = 1
    )
    @Test
    void declineIncomeInvitation(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .goToFriendsPage()
                .declineIncomeInv();
    }

    @User(
            friends = 1
    )
    @Test
    void friendShouldBePresentInFriendsTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .goToFriendsPage()
                .checkExistingFriends(user.testData().friends().getFirst().username());
    }

    @User
    @Test
    void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .goToFriendsPage()
                .checkNoExistingFriends();
    }

    @User(
            incomeInvitations = 1
    )
    @Test
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .goToFriendsPage()
                .checkExistingInvitations(valueOf(user.testData().incomeInvitations().getFirst().username()));
    }

    @User(
            outcomeInvitations = 1
    )
    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .goToallPeoplesPage()
                .checkInvitationSentToUser(valueOf(user.testData().outcomeInvitations().getFirst().username()));
    }
}
