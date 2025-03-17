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

    public List<Tuple> getAll() {
        return this.tuples;
    }

    public Tuple get(int index) {
        return this.tuples.get(index);
    }

    public int size() {
        return this.tuples.size();
    }

    public boolean isEmpty() {
        return this.tuples.isEmpty();
    }

    public boolean isNotEmpty() {
        return !this.isEmpty();
    }

    public Tuple first() {
        return this.get(0);
    }

    public Tuple last() {
        return this.get(this.size() - 1);
    }

    public Tuple random() {
        return this.get(this.random.nextInt(this.size()));
    }

    public Tuple find(String key, Object value) {
        for (Tuple tuple : this.tuples) {
            if (tuple.get(key).equals(value)) {
                return tuple;
            }
        }
        return null;
    }

    public void forEach(Consumer<Tuple> consumer) {
        this.tuples.forEach(consumer);
    }

    /**
     * Convertir le résultat en JSON
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
