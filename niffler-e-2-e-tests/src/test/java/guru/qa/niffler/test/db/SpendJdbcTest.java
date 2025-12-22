package guru.qa.niffler.test.db;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import org.hibernate.type.TrueFalseConverter;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class SpendJdbcTest {

  @Test
  void txTest() {
    SpendDbClient spendDbClient = new SpendDbClient();

    SpendJson spend = spendDbClient.createSpend(
        new SpendJson(
            null,
            new Date(),
            new CategoryJson(
                null,
                "cat-name-tx-30",
                "test1",
                false
            ),
            CurrencyValues.RUB,
            1000.0,
            "spend-name-tx",
            "test1"
        )
    );
    System.out.println(spend);
  }
}
