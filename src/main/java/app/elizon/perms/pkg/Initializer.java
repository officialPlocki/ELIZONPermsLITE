package app.elizon.perms.pkg;

import co.plocki.mysql.MySQLDriver;
import co.plocki.mysql.MySQLTable;

public class Initializer {

    private static MySQLDriver driver;

    private static MySQLTable.fin playerTable;
    private static MySQLTable.fin groupTable;

    public void init() {
        driver = new MySQLDriver();

        MySQLTable table = new MySQLTable();
        table.prepare("permPlayer", "uuid", "permissionsJson", "groupsJson");
        playerTable = table.build();

        MySQLTable table1 = new MySQLTable();
        table1.prepare("permGroups", "name", "permissionsJson");
        groupTable = table1.build();



        /***
         * Premium:
         * Adds functionality for rank traces, prefixes, suffixes, rank logs and rank & permission timings
         * Adds discord bot with rank request and mc-link
         * Adds rank priorities, sorting and permission inherits
         * Adds default ranks for players
         */

    }

    public static MySQLTable.fin getGroupTable() {
        return groupTable;
    }

    public static MySQLTable.fin getPlayerTable() {
        return playerTable;
    }

    public MySQLDriver getDriver() {
        return driver;
    }

}
