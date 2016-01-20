package org.n3r.sandbox.db.ibatis;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.HashMap;

public class IbatisTest {
    @Test
    public void testSimple() throws SQLException, IOException {
        String resource = "org/n3r/sandbox/db/ibatis/sqlmap-config.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        SqlMapClient sqlmap = SqlMapClientBuilder.buildSqlMapClient(reader);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("dataId", "a");
        Object obj = sqlmap.queryForObject("test1", param);
        System.out.println(obj);
    }
}
