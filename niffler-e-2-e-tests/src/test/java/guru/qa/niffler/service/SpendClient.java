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

  @Nullable
  SpendJson getSpend(String id);

  List<SpendJson> allSpends(String username,
                            @Nullable CurrencyValues currency,
                            @Nullable Date from,
                            @Nullable Date to);

  void removeSpends(String username, String... ids);

  CategoryJson createCategory(CategoryJson category);

  CategoryJson updateCategory(CategoryJson category);

  List<CategoryJson> allCategories(String username);

  void removeCategory(CategoryJson category);

  SpendJson findSpendByUsernameAndSpendDescription(String username, String description);
}
