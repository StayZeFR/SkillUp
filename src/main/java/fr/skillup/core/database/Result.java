package fr.skillup.core.database;

import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

public class Result {

    private SecureRandom random;
    private final List<Class<?>> types;
    private final List<Tuple> tuples;

    public Result(ResultSet resultSet, Class<?>... clazz) throws SQLException {
        this.random = new SecureRandom();
        this.tuples = new ArrayList<>();
        this.types = Arrays.asList(clazz);
        this.scan(resultSet);
    }

    /**
     * Scan le ResultSet et le convertit en une liste de tuples
     *
     * @param resultSet : ResultSet à scanner
     */
    private void scan(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            Map<String, Object> tuple = new HashMap<>();
            for (int i = 0; i < this.types.size(); i++) {
                String column = resultSet.getMetaData().getColumnLabel(i + 1);
                Object value = resultSet.getObject(i + 1);
                tuple.put(column, value);
            }
            this.tuples.add(new Tuple(this.types, tuple));
        }
    }

    /**
     * Récupère le résultat sous forme de liste de tuples
     *
     * @return liste de tuples
     */
    public List<Tuple> getAll() {
        return this.tuples;
    }

    /**
     * Récupère le tuple à l'index donné
     *
     * @param index : index du tuple
     * @return Tuple : tuple
     */
    public Tuple get(int index) {
        return this.tuples.get(index);
    }

    /**
     * Récupère la taille de la liste de tuples
     *
     * @return int : taille de la liste
     */
    public int size() {
        return this.tuples.size();
    }

    /**
     * Vérifie si la liste de tuples est vide
     *
     * @return true si la liste est vide, false sinon
     */
    public boolean isEmpty() {
        return this.tuples.isEmpty();
    }

    /**
     * Vérifie si la liste de tuples n'est pas vide
     *
     * @return true si la liste n'est pas vide, false sinon
     */
    public boolean isNotEmpty() {
        return !this.isEmpty();
    }

    /**
     * Récupère le premier tuple de la liste
     *
     * @return Tuple : premier tuple
     */
    public Tuple first() {
        return this.get(0);
    }

    /**
     * Récupère le dernier tuple de la liste
     *
     * @return Tuple : dernier tuple
     */
    public Tuple last() {
        return this.get(this.size() - 1);
    }

    /**
     * Récupère un tuple aléatoire de la liste
     *
     * @return Tuple : tuple aléatoire
     */
    public Tuple random() {
        return this.get(this.random.nextInt(this.size()));
    }

    /**
     * Récupère un tuple en fonction d'une clé et d'une valeur
     *
     * @param key   : clé
     * @param value : valeur
     * @return Tuple : tuple correspondant à la clé et à la valeur
     */
    public Tuple find(String key, Object value) {
        for (Tuple tuple : this.tuples) {
            if (tuple.get(key).equals(value)) {
                return tuple;
            }
        }
        return null;
    }

    /**
     * Parcourir la liste de tuples et appliquer une fonction à chaque tuple
     *
     * @param consumer : fonction à appliquer
     */
    public void forEach(Consumer<Tuple> consumer) {
        this.tuples.forEach(consumer);
    }

    /**
     * Convertir le résultat en JSON
     *
     * @return String : JSON
     */
    public String toJson() {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < this.size(); i++) {
            json.append(this.get(i).toJson());
            if (i < this.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }

}
