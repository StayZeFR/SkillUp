package fr.skillup.core.database;

import fr.skillup.core.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public abstract class Database {

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
                Logger.getLogger(Database.class.getName()).severe("Impossible de se connecter à la base de données");
            }
        }
        return Database.connection;
    }

}
