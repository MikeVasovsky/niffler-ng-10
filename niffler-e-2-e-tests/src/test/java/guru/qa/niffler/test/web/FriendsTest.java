//package guru.qa.niffler.test.web;
//
//import com.codeborne.selenide.Selenide;
//import guru.qa.niffler.config.Config;
//import guru.qa.niffler.jupiter.annotation.UserType;
//import guru.qa.niffler.jupiter.annotation.meta.WebTest;
//import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
//import guru.qa.niffler.page.LoginPage;
//import org.junit.jupiter.api.Test;
//
//import static guru.qa.niffler.jupiter.annotation.UserType.Type.*;
//
//@WebTest
//public class FriendsTest {
//
//  private static final Config CFG = Config.getInstance();
//
//  @Test
//  void friendShouldBePresentInFriendsTable(@UserType(userType = WITH_FRIEND) StaticUser user) {
//    Selenide.open(CFG.frontUrl(), LoginPage.class)
//        .successLogin(user.username(), user.password())
//        .checkThatPageLoaded()
//        .friendsPage()
//        .checkExistingFriends(user.friend());
//  }
//
//  @Test
//  void friendsTableShouldBeEmptyForNewUser(@UserType(userType = EMPTY) StaticUser user) {
//    Selenide.open(CFG.frontUrl(), LoginPage.class)
//        .successLogin(user.username(), user.password())
//        .checkThatPageLoaded()
//        .friendsPage()
//        .checkNoExistingFriends();
//  }
//
//  @Test
//  void incomeInvitationBePresentInFriendsTable(@UserType(userType = WITH_INCOME_REQUEST) StaticUser user) {
//    Selenide.open(CFG.frontUrl(), LoginPage.class)
//        .successLogin(user.username(), user.password())
//        .checkThatPageLoaded()
//        .friendsPage()
//        .checkExistingInvitations(user.income());
//  }
//
//  @Test
//  void outcomeInvitationBePresentInAllPeoplesTable(@UserType(userType =WITH_OUTCOME_REQUEST) StaticUser user) {
//    Selenide.open(CFG.frontUrl(), LoginPage.class)
//        .successLogin(user.username(), user.password())
//        .checkThatPageLoaded()
//        .allPeoplesPage()
//        .checkInvitationSentToUser(user.outcome());
//  }
//}
