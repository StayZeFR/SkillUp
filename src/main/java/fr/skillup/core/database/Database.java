package fr.skillup.core.database;

import fr.skillup.core.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String HOST = Config.get("db.host");
    private static final String PORT = Config.get("db.port");
    private static final String NAME = Config.get("db.name");
    private static final String USER = Config.get("db.user");
    private static final String PASSWORD = Config.get("db.password");

    private static Connection connection;

    public static Connection getConnection() {
        if (Database.connection == null) {
            try {
                Database.connection = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + NAME, USER, PASSWORD);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return Database.connection;
    }

}
