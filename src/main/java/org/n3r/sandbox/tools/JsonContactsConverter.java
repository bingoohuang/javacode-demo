package org.n3r.sandbox.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.sql.*;

public class JsonContactsConverter {
    public static void main(String[] args) throws IOException, SQLException {
        String json = FileUtils.readFileToString(new File("contacts.json"), Charsets.UTF_8);
        JSONArray data = JSON.parseObject(json).getJSONArray("data");

        toH2(data);
        //toCsv(data);
    }

    private static void toH2(JSONArray data) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:h2:~/contacts", "sa", "")) {
            String dropTableSql = "drop table if exists contacts";
            try (Statement statement = (conn.createStatement())) {
                statement.execute(dropTableSql);
            }

            String createTableSql = "create table contacts(age int,assignment_id int,birth_date char(19)," +
                    "company varchar(100),company_id varchar(100),email_address varchar(100)," +
                    "employee_number varchar(100) PRIMARY KEY,"  /*,first_name varchar(100),full_name varchar(100),*/ +
                    "highest_degree varchar(100),hire_date char(19),last_name varchar(100),mobile varchar(100)," +
                    "nt_account varchar(100),office varchar(100),org_name varchar(100),organization_id int," +
                    "person_id int,sbu varchar(100),sbu_id varchar(100),seat_no varchar(100)," +
                    "supervisor_id int,supervisor_name varchar(100),working_location varchar(100))";
            try (Statement statement = (conn.createStatement())) {
                statement.execute(createTableSql);
            }

            String insertSql = "insert into contacts values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement ps = (conn.prepareStatement(insertSql))) {
                for (int i = 0, ii = data.size(); i < ii; ++i) {
                    JSONObject item = data.getJSONObject(i);
                    int j = 0;
                    ps.setInt(++j, item.getIntValue("age")); // "age":45.0
                    ps.setInt(++j, item.getIntValue("assignment_id")); // "assignment_id":463.0
                    ps.setString(++j, convertDate(item.getString("birth_date"))); // "birth_date":"1910-11-19T00:00:00",
                    ps.setString(++j, item.getString("company")); // "company":"",
                    ps.setString(++j, item.getString("company_id")); // "company_id":"01",
                    ps.setString(++j, item.getString("email_address")); // "email_address":"",
                    ps.setString(++j, item.getString("employee_number")); //  "employee_number":"",
                    // ps.setString(++j, item.getString("first_name")); //  "first_name":"",
                    // ps.setString(++j, item.getString("full_name")); //  "full_name":"",
                    ps.setString(++j, item.getString("highest_degree")); //  "highest_degree":"Post-graduate",
                    ps.setString(++j, convertDate(item.getString("hire_date"))); // "hire_date":"",
                    ps.setString(++j, item.getString("last_name")); // "last_name":"",
                    ps.setString(++j, item.getString("mobile")); // "mobile":"",
                    ps.setString(++j, item.getString("nt_account")); // "nt_account":"",
                    ps.setString(++j, item.getString("office")); // "office":"",
                    ps.setString(++j, item.getString("org_name")); // "org_name":"",
                    ps.setInt(++j, item.getIntValue("organization_id")); // "organization_id":123.0,
                    ps.setInt(++j, item.getIntValue("person_id")); // "person_id":123.0,
                    ps.setString(++j, item.getString("sbu")); // "sbu":"XXX",
                    ps.setString(++j, item.getString("sbu_id")); // "sbu_id":"99",
                    ps.setString(++j, item.getString("seat_no")); // "seat_no":"1111",
                    ps.setInt(++j, item.getIntValue("supervisor_id")); // "supervisor_id":123.0,
                    ps.setString(++j, item.getString("supervisor_name")); // "supervisor_name":"XXXX(1234)",
                    ps.setString(++j, item.getString("working_location"));  //"working_location":"XX"}
                    ps.execute();
                }
            }
        }
    }

    private static String convertDate(String dateString) {
        if (dateString == null || dateString.length() < 10) return dateString;

        // 1970-03-29T00:00:00
        return dateString.substring(0, 10);
    }


    private static void toCsv(JSONArray data) throws FileNotFoundException {
        try (PrintStream ps = new PrintStream(new FileOutputStream(new File("contacts.csv")))) {
            ps.println("age,assignment_id,birth_date,company,company_id,email_address,employee_number," +
                    /*first_name,full_name,*/"highest_degree,hire_date,last_name,mobile,nt_account,office," +
                    "org_name,organization_id,person_id,sbu,sbu_id,seat_no," +
                    "supervisor_id,supervisor_name,working_location");

            for (int i = 0, ii = data.size(); i < ii; ++i) {
                JSONObject item = data.getJSONObject(i);
                ps.append("" + item.getIntValue("age")).append(','); // "age":55.0
                ps.append("" + item.getIntValue("assignment_id")).append(','); // "assignment_id":123.0
                ps.append(convertDate(item.getString("birth_date"))).append(','); // "birth_date":"1911-11-11T00:00:00",
                ps.append(item.getString("company")).append(','); // "company":"xxx",
                ps.append(item.getString("company_id")).append(','); // "company_id":"11",
                ps.append(item.getString("email_address")).append(','); // "email_address":"xxx@yyyyy.com",
                ps.append(item.getString("employee_number")).append(','); //  "employee_number":"1234",
                // ps.append(item.getString("first_name")).append(','); //  "first_name":"xxxx yyyy",
                // ps.append(item.getString("full_name")).append(','); //  "full_name":"xxxx yyyy",
                ps.append(item.getString("highest_degree")).append(','); //  "highest_degree":"Post-graduate",
                ps.append(convertDate(item.getString("hire_date"))).append(','); // "hire_date":"2111-04-06T00:00:00",
                ps.append(item.getString("last_name")).append(','); // "last_name":"XXX",
                ps.append(item.getString("mobile")).append(','); // "mobile":"12345678901",
                ps.append(item.getString("nt_account")).append(','); // "nt_account":"xxxx",
                ps.append(item.getString("office")).append(','); // "office":"11111",
                ps.append(item.getString("org_name")).append(','); // "org_name":"xxxxx",
                ps.append("" + item.getIntValue("organization_id")).append(','); // "organization_id":123.0,
                ps.append("" + item.getIntValue("person_id")).append(','); // "person_id":123.0,
                ps.append(item.getString("sbu")).append(','); // "sbu":"XX",
                ps.append(item.getString("sbu_id")).append(','); // "sbu_id":"11",
                ps.append(item.getString("seat_no")).append(','); // "seat_no":"1111",
                ps.append("" + item.getIntValue("supervisor_id")).append(','); // "supervisor_id":1111.0,
                ps.append(item.getString("supervisor_name")).append(','); // "supervisor_name":"XXX(1111)",
                ps.append(item.getString("working_location")).append(',');  //"working_location":"XXX"}
                ps.println();
            }
        }
    }

}
