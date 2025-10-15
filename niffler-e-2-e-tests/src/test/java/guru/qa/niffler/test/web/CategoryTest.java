package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.CategoryGenerateExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(CategoryGenerateExtension.class)
public class CategoryTest {
    private static final Config CFG = Config.getInstance();

    @User(
            username = "user_with_friend"
         //   categories = @Category(archived = true)
    )
    @Test
    public void checkCorrectMessageAfterArchiveCategory(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("user_with_friend", "111")
                .goToProfilePage()
                .showArchiveCategories()
                .findCategory(category.name());
    }

    @User(
            username = "user_with_friend",
            categories = @Category(archived = false)
    )
    @Test
    public void checkCorrectMessageAfterUnarchiveCategory(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("user_with_friend", "111")
                .goToProfilePage()
                .addNewCategory(category.name())
                .archiveCategory(category.name())
                .unarchiveCategory(category.name())
                .haveRightMessageAfterUnarchiveCategory(String.format("Category %s is unarchived", category.name()));
    }
}
