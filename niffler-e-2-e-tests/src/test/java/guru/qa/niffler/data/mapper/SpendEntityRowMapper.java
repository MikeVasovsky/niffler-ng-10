package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SpendEntityRowMapper implements RowMapper<SpendEntity> {

    public final static SpendEntityRowMapper instance = new SpendEntityRowMapper();

    private SpendEntityRowMapper() {
    }

    @Override
    public SpendEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        SpendEntity spendResult = new SpendEntity();
        spendResult.setId(rs.getObject("id", UUID.class));
        spendResult.setUsername(rs.getString("username"));
        spendResult.setSpendDate(rs.getDate("date"));
        spendResult.setCurrency(rs.getObject("currency", CurrencyValues.class));
        spendResult.setAmount(rs.getDouble("amount"));
        spendResult.setDescription(rs.getString("description"));
        return spendResult;
    }


}
