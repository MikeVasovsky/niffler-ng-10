package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.mapper.CategoryEntityRowMapper;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class SpendEntityRepositoryJdbc implements SpendRepository {

    private static final String URL = Config.getInstance().authJdbcUrl();

    @Override
    public SpendEntity create(SpendEntity spend) {
        return null;
    }

    @Override
    public List<SpendEntity> findByCategoryId (UUID id) {
        try(PreparedStatement ps = holder(URL).connection().prepareStatement(
                "select * from spend s join category c on s.category_id = c.id where c.id  = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()){
                List<SpendEntity> resultSpend = new ArrayList<>();
                CategoryEntity ce = null;
                while (rs.next()){
                    if (ce == null){
                        ce = CategoryEntityRowMapper.instance.mapRow(rs,1);
                    }
                    SpendEntity resEn = new SpendEntity();
                    resEn.setId(rs.getObject("s.id", UUID.class));
                    resEn.setUsername(rs.getString("s.username"));
                    resEn.setCurrency(CurrencyValues.valueOf(rs.getString("s.currency")));
                    resEn.setSpendDate(rs.getDate("s.spend_date"));
                    resEn.setAmount(rs.getDouble("s.amount"));
                    resEn.setDescription(rs.getString("s.description"));
                    resEn.setCategory(ce);
                    resultSpend.add(resEn);

                }
                return resultSpend;

                }
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
