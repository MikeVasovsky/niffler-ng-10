package guru.qa.niffler.data.mapper.springmapper;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpendEntityResultSetExtractor implements ResultSetExtractor<List<SpendEntity>> {

    public final static SpendEntityResultSetExtractor instance = new SpendEntityResultSetExtractor();

    private SpendEntityResultSetExtractor() {
    }

    @Override
    public List<SpendEntity> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<SpendEntity> spendList = new ArrayList<>();
        while (rs.next()) {
            SpendEntity spend = new SpendEntity();
            spend.setId(rs.getObject("s_id", UUID.class));
            spend.setUsername(rs.getString("s_username"));
            spend.setSpendDate(rs.getDate("s_date"));
            spend.setCurrency(CurrencyValues.valueOf(rs.getString("s_currency")));
            spend.setAmount(rs.getDouble("s_amount"));
            spend.setDescription(rs.getString("s_desc"));

            CategoryEntity category = new CategoryEntity();
            category.setId(rs.getObject("c_id", UUID.class));
            category.setName(rs.getString("c_name"));
            category.setUsername(rs.getString("c_username"));
            category.setArchived(rs.getBoolean("c_archived"));
            spend.setCategory(category);
            spendList.add(spend);
        }
        return spendList;
    }
}

