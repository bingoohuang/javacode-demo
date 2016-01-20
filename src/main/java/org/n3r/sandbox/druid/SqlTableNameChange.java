package org.n3r.sandbox.druid;


import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.visitor.SQLASTVisitorAdapter;

public class SqlTableNameChange {
    public static void main(String[] args) {
        String originSql = "select m.a,m.b from mytable m";
        MySqlStatementParser parser = new MySqlStatementParser(originSql);
        SQLStatement statement = parser.parseStatement();
        statement.accept(new SQLASTVisitorAdapter() {
            @Override
            public boolean visit(SQLSelectQueryBlock x) {
                SQLTableSource from = x.getFrom();
                from.accept(new SQLASTVisitorAdapter() {
                    @Override
                    public boolean visit(SQLIdentifierExpr x) {
                        x.setName("mytablexx");
                        return false;
                    }
                });

                return false;
            }

        });

        /*
        print:
        SELECT m.a, m.b
        FROM mytablexx m
         */
        String sql = SQLUtils.toSQLString(statement);
        System.out.println(sql);
    }
}
