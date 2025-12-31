package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.springframework.data.web.PagedModel;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Date;
import java.util.List;

public interface SpendApi {
    @POST("internal/spends/add")
    Call<SpendJson> addSpend(@Body SpendJson spend);

    @GET("internal/spends/{id}")
    Call<SpendJson> getSpend(@Path("id") String id);

    @GET("internal/spends/all")
    Call<List<SpendJson>> getSpends(@Query("username") String username,
                                    @Query("filterCurrency") CurrencyValues filterCurrency,
                                    @Query("from") Date from,
                                    @Query("to") Date to);

    @PATCH("internal/spends/edit")
    Call<SpendJson> editSpend(@Body SpendJson spend);

    @DELETE("internal/spends/remove")
    Call<Void> removeSpends(@Query("username") String username, @Query("ids") List<String> ids);

    @GET("internal/categories/all")
    Call<List<CategoryJson>> getCategories(@Query("username") String username);

    @POST("internal/categories/add")
    Call<CategoryJson> addCategory(@Body CategoryJson category);

    @PATCH("internal/categories/update")
    Call<CategoryJson> updateCategory(@Body CategoryJson category);

    @DELETE("internal/categories/remove")
    Call<Void> removeCategories(@Query("username") String username, @Query("ids") List<String> ids);

}
