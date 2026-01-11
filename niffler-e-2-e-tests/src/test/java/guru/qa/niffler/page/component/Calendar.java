package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Date;

@ParametersAreNonnullByDefault
public class Calendar {
    private final SelenideElement self;
    private final SelenideElement dateInputFld;

    public Calendar(SelenideElement self) {
        this.self = self;
        this.dateInputFld = self.$("input[name='date']");
    }


    public Calendar setDateInCalendar(Date date) {
        dateInputFld.val(String.valueOf(date));
        return this;
    }
}
