package fr.skillup.core.model;

import fr.skillup.core.database.Database;
import fr.skillup.core.database.Result;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public abstract class Model {

    private static final Map<Class<? extends Model>, Model> models = new HashMap<>();
    private final Connection connection;

    protected Model() {
        this.connection = Database.getConnection();
    }

    /**
     * Récupère une instance de la classe Model
     * @param clazz : la classe du modèle
     * @return une instance de la classe Model
     */
    public static <T extends Model> T get(Class<T> clazz) {
        if (!Model.models.containsKey(clazz)) {
            try {
                Model.models.put(clazz, clazz.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                Logger.getLogger(Model.class.getName()).severe(e.getMessage());
            }
        }
        return clazz.cast(Model.models.get(clazz));
    }

    /**
     * Exécute une requête de type SELECT
     * @param query : la requête SQL
     * @param params : les paramètres de la requête
     * @param clazz : les classes des objets à instancier
     * @return un objet Result
     */
    protected Result select(String query, List<Object> params, Class<?>... clazz) {
        Result result = null;
        PreparedStatement statement = null;
        try {
            statement = this.connection.prepareStatement(query);
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            ResultSet resultSet = statement.executeQuery();
            result = new Result(resultSet, clazz);
            statement.close();
        } catch (SQLException e) {
            Logger.getLogger(Model.class.getName()).severe(e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    Logger.getLogger(Model.class.getName()).severe(e.getMessage());
                }
            }
        }
        return result;
    }

    /**
     * Exécute une requête de type SELECT
     * @param query : la requête SQL
     * @param clazz : les classes des objets à instancier
     * @return un objet Result
     */
    protected Result select(String query, Class<?>... clazz) {
        return this.select(query, List.of(), clazz);
    }

}
