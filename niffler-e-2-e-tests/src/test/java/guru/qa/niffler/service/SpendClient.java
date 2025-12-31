package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;

public interface SpendClient {

    SpendJson createSpend(SpendJson spend);

    SpendJson editSpend(SpendJson spend);

    SpendJson getSpend(String id);

    List<SpendJson> allSpends(String username,
                              @Nullable CurrencyValues currency,
                              @Nullable Date from,
                              @Nullable Date to);

    void removeSpends(String username, String... ids);

    List<CategoryJson> getCategories(String username);

    CategoryJson createCategory(CategoryJson category);

    CategoryJson updateCategory(CategoryJson category);
}
