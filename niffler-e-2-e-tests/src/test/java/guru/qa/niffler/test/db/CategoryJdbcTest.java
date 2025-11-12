package guru.qa.niffler.test.db;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class CategoryJdbcTest {
    @Test
    void category() {
        SpendDbClient spendDbClient = new SpendDbClient();

        CategoryJson category = spendDbClient.createCategory(
                new CategoryJson(
                        null,
                        "testName",
                        "mikeVasTest"
                        ,false
                )
        );
        System.out.println(category);
    }
}
