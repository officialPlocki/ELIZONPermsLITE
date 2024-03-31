package co.plocki.mysql;


import app.elizon.perms.pkg.Initializer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class MySQLDelete {

    private String statement;
    private final HashMap<String, String> requirements;

    public MySQLDelete() {
        this.requirements = new HashMap<>();
    }

    /**
     * Prepare an update statement.
     *
     * @param table The table you want to delete something from.
     */
    public void prepare(String table) {
        statement = "DELETE FROM " + table;
    }

    /**
     * It adds a requirement to the push
     *
     * @param value       The value to be pushed to the database.
     * @param requirement The requirement to be added.
     */
    public void addRequirement(String value, Object requirement) {
        requirements.put(value, requirement.toString());
    }

    /**
     * It takes the statement and the requirements and combines them into one statement
     */
    public void execute() {
        if(!requirements.isEmpty()) {
            if(requirements.size() > 0) {
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
            }
        }
        statement = statement.replaceFirst(";", "") + ";";
        try(Connection connection = new Initializer().getDriver().getHikariDataSource().getConnection()) {
            connection.prepareStatement(statement).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}