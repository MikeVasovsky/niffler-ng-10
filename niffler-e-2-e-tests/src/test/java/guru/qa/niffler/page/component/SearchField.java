package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Selenide.$;

public class SearchField {
    private final SelenideElement self;
    private final SelenideElement clearBtn = $("button[id='input-clear']");

    public SearchField(SelenideElement self) {
        this.self = self;
    }

    public SearchField search(String query) {
        self.val(query).pressEnter();
        return this;
    }

    public SearchField clearIfNotEmpty() {
        if (self.is(not(empty))) {
            clearBtn.click();
            self.shouldBe(empty);
        }
        return this;
    }

    public SearchField inputText(String val){
        self.val(val);
        return this;
    }
}
