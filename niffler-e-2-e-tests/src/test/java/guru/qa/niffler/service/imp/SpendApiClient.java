package guru.qa.niffler.service.imp;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;
import org.springframework.data.web.PagedModel;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient implements SpendClient {

  private static final Config CFG = Config.getInstance();

  private final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(CFG.spendUrl())
      .addConverterFactory(JacksonConverterFactory.create())
      .build();

  private final SpendApi spendApi = retrofit.create(SpendApi.class);

  @Override
  public SpendJson createSpend(SpendJson spend) {
    final Response<SpendJson> response;
    try {
      response = spendApi.addSpend(spend)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(201, response.code());
    return response.body();
  }

  @Override
  public CategoryJson createCategory(CategoryJson category) {
    final Response<CategoryJson> response;
    try {
      response = spendApi.addCategory(category)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Override
  public Optional<CategoryJson> findCategoryByNameAndUsername(String categoryName, String username) {
    return Optional.empty();
  }

  @Override
  public SpendJson getUserById(String id, String userName) {
    return null;
  }

  @Override
  public CategoryJson updateCategory(CategoryJson category) {
    final Response<CategoryJson> response;
    try {
      response = spendApi.updateCategory(category)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public SpendJson editSpend(SpendJson spend) {
    final Response<SpendJson> response;
    try {
      response = spendApi.editSpend(spend)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Override
  public Void deleteSpends(String username, List<String> ids) {
    return null;
  }

  @Override
  public PagedModel<SpendJson> getSpends(String username, CurrencyValues filterCurrency, Date from, Date to, String searchQuery, Integer page, Integer size, List<String> sort) {
    return null;
  }

  public SpendJson getSpend(String id, String username) {
    final Response<SpendJson> response;
    try {
      response = spendApi.getSpend(id, username)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public List<SpendJson> getSpends(String username,
                                   CurrencyValues currency,
                                   Date from,
                                   Date to) {
    final Response<List<SpendJson>> response;
    try {
      response = spendApi.getSpends(username, currency, from, to)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public void removeSpends(String username, String... ids) {
    final Response<Void> response;
    try {
      response = spendApi.deleteSpends(username, Arrays.stream(ids).toList())
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
  }

  public List<CategoryJson> getCategories(String username,boolean excludeArchived) {
    final Response<List<CategoryJson>> response;
    try {
      response = spendApi.getCategories(username, excludeArchived)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Override
  public CategoryJson addCategory(CategoryJson category) {
    return null;
  }
}
