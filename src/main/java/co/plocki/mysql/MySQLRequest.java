package co.plocki.mysql;

import app.elizon.perms.pkg.Initializer;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MySQLRequest {

    private String statement;
    private final HashMap<String, String> requirements;

    public MySQLRequest() {
        requirements = new HashMap<>();
    }

    public void prepare(String table) {
        statement = "SELECT * FROM " + table;
    }

    /**
     * Prepare a SELECT statement.
     *
     * @param select The columns you want to select.
     * @param table  The table you want to select from
     */
    public void prepare(String select, String table) {
        statement = "SELECT " + select + " FROM " + table;
    }

    /**
     * It adds a requirement to the request
     *
     * @param value       The column name
     * @param requirement The value of the requirement.
     */
    public void addRequirement(String value, Object requirement) {
        requirements.put(value, requirement.toString());
    }

    /**
     * It executes the statement and returns the result
     *
     * @return A MySQLResult object.
     */
    public MySQLResponse execute() {
        if(!requirements.isEmpty()) {
            if(requirements.size() > 1) {
                final int[] i = {0};
                requirements.forEach((s, s2) -> {
                    if(!(i[0] == (requirements.size() - 1))) {
                        if(i[0] == 0) {
                            statement = statement + " WHERE ";
                        }
                        statement = statement + s + " = '" + s2 + "' AND ";
                    } else {
                        if(i[0] == 0) {
                            statement = statement + " WHERE ";
                        }
                        statement = statement + s + " = '" + s2 + "';";
                    }
                    i[0] = i[0] + 1;
                });
            } else {
                final int[] i = {0};
                requirements.forEach((s, s2) -> {
                    statement = statement + " WHERE ";
                    statement = statement + s + " = '" + s2 + "';";
                });
            }
        }
        statement = statement.split(";")[0] + ";";
        try(Connection connection = new Initializer().getDriver().getHikariDataSource().getConnection()) {
            PreparedStatement exec = connection.prepareStatement(statement);
            List<HashMap<String, String>> vals = new ArrayList<>();
            try {
                ResultSet result = exec.executeQuery();
                while (result.next()) {
                    ResultSetMetaData data = result.getMetaData();
                    HashMap<String, String> values = new HashMap<>();
                    for(int i = 1; i <= data.getColumnCount(); i++) {
                        values.put(data.getColumnName(i), result.getString(i));
                    }
                    vals.add(values);
                }
                return new MySQLResponse(vals);
            } catch (IndexOutOfBoundsException e) {
                return new MySQLResponse();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}