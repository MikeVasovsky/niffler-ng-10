package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.UserType;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.annotation.UserType.Type.*;

@WebTest
public class FriendsTest {

    private static final Config CFG = Config.getInstance();

    @User(
            friends = 1
    )
    @Test
    void friendShouldBePresentInFriendsTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .goToFriendsPage()
                .checkThatFriendExist(user.testData().friends().getFirst().username());
    }

    @User
    @Test
    void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .goToFriendsPage()
                .checkThatFriendsListIsEmpty();
    }

    @User(
            incomeInvitations = 1
    )
    @Test
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .goToFriendsPage()
                .checkThatIncomeInviteExist(user.testData().incomeInvitations().getFirst().username());
    }

    @User
    @Test
    void seatchUserBysearchField(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("mikeVasovsky", "1111")
                .checkThatPageLoaded()
                .goToPeoplePage()
                .searchPeople(user.username())
                .checkThatTableContains(user.username());
    }
}