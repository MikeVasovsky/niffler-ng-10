package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.mapper.SpendEntityRowMapper;
import guru.qa.niffler.data.mapper.springmapper.SpendEntityResultSetExtractor;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SpendDaoRepositorySpringJdbc implements SpendRepository {

    private static final Config CFG = Config.getInstance();
    private static final String URL = CFG.spendJdbcUrl();

    @Override
    public SpendEntity create(SpendEntity spend) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(URL));
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) " +
                            "VALUES ( ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, spend.getUsername());
            ps.setDate(2, new java.sql.Date(spend.getSpendDate().getTime()));
            ps.setString(3, spend.getCurrency().name());
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, spend.getCategory().getId());
            return ps;
        }, kh);

        final UUID generatedKey = (UUID) kh.getKeys().get("id");
        spend.setId(generatedKey);
        return spend;
    }

    @Override
    public List<SpendEntity> findSpendsAndCategoriesById(UUID categoryId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(URL));
        return jdbcTemplate.query(
                "select\n" +
                        "  s.id as s_id,\n" +
                        "  s.username as s_username,\n" +
                        "  s.spend_date as s_date,\n" +
                        "  s.currency as s_currency,\n" +
                        "  s.amount as s_amount,\n" +
                        "  s.description as s_desc,\n" +
                        "  s.category_id as s_catId,\n" +
                        "  c.id as c_id,\n" +
                        "  c.name as c_name,\n" +
                        "  c.username as c_username,\n" +
                        "  c.archived as c_archived\n" +
                        "from spend s \n" +
                        "join category c on s.category_id = c.id\n" +
                        "where c.id =  ?",
                SpendEntityResultSetExtractor.instance, categoryId);
    }


}
