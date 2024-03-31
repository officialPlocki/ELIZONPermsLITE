package co.plocki.mysql;

import app.elizon.perms.pkg.Initializer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MySQLInsert {

    private final StringBuilder statement;

    public MySQLInsert() {
        statement = new StringBuilder();
    }

    /**
     * It creates a statement that will insert a row into a table if the row doesn't already exist
     *
     * @param information This is the enum that contains the table name and the values that are required.
     */
    public void prepare(MySQLTable.fin information, Object... values) {
        statement.append("INSERT INTO ").append(information.getTableName()).append("( ");
        final int[] i = {0};
        for(String value : information.getValues()) {
            if(!(i[0] == (values.length - 1))) {
                statement.append(value).append(", ");
            } else {
                statement.append(value).append(") VALUES (");
            }
            i[0] = i[0] + 1;
        }
        i[0] = 0;
        for(Object value : values) {
            if(!(i[0] == (values.length - 1))) {
                statement.append(" '").append(value).append("' ").append(", ");
            } else {
                statement.append(" '").append(value).append("' ").append(");");
            }
            i[0] = i[0] + 1;
        }
    }

    public void prepare(MySQLTable.fin information, List<Object> values) {
        statement.append("INSERT INTO ").append(information.getTableName()).append("( ");
        final int[] i = {0};
        for(String value : information.getValues()) {
            if(!(i[0] == (values.size() - 1))) {
                statement.append(value).append(", ");
            } else {
                statement.append(value).append(") VALUES (");
            }
            i[0] = i[0] + 1;
        }
        i[0] = 0;
        for(Object value : values) {
            if(!(i[0] == (values.size() - 1))) {
                statement.append(" '").append(value).append("' ").append(", ");
            } else {
                statement.append(" '").append(value).append("' ").append(");");
            }
            i[0] = i[0] + 1;
        }
    }

    /**
     * It executes a SQL statement.
     */
    public void execute() {
        if(statement.toString().equalsIgnoreCase("empty")) return;
        try(Connection connection = new Initializer().getDriver().getHikariDataSource().getConnection()) {
            connection.prepareStatement(statement.toString()).executeLargeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}