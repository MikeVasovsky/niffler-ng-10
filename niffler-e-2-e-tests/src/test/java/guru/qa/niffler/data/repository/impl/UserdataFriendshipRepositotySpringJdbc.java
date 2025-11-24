package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.mapper.springmapper.UserdataFriendshipResultSetExtractor;
import guru.qa.niffler.data.repository.UserdataFriendshipRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.UUID;

public class UserdataFriendshipRepositotySpringJdbc implements UserdataFriendshipRepository {

    private static final String URL = Config.getInstance().userdataUrl();


    @Override
    public List<FriendshipEntity> getAllFriendshipRequestById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(URL));

        return jdbcTemplate.query(
                """
                        select 
                        f.requester_id as requester_id,
                           f.addressee_id as addressee_id, 
                            f.created_date as created_date, 
                            f.status as status, 
                            r.id as r_id,
                            r.username as r_username,
                            r.currency as r_currency, 
                            a.id as a_id, 
                            a.username as a_username, 
                            a.currency as a_currency 
                        from friendship f 
                        join \"user\" r on f.requester_id = r.id 
                        join \"user\" a on f.addressee_id = a.id 
                        where f.requester_id = ?
                        """,
                UserdataFriendshipResultSetExtractor.instance,id
        );
    }
}
