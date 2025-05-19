package fr.skillup.core.database;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Tuple {

    private final List<Class<?>> types;
    private final Map<String, Object> tuple;

    public Tuple(List<Class<?>> types, Map<String, Object> tuple) {
        this.tuple = tuple;
        this.types = types;
    }

    /**
     * Récupère la valeur associée à une clé dans le tuple et la cast dans le type attendu
     *
     * @param key : Clé de la valeur à récupérer
     * @return Valeur associée à la clé
     */
    public <T> T get(String key) {
        if (!this.tuple.containsKey(key)) {
            throw new IllegalArgumentException("Clé non trouvé : " + key);
        }

        int index = this.types.indexOf(this.tuple.get(key).getClass());
        if (index == -1) {
            throw new IllegalStateException("Type mismatch for key : " + key);
        }

        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) this.types.get(index);
        return clazz.cast(this.tuple.get(key));
    }

    /**
     * Convertit le tuple en JSON
     *
     * @return JSON représentant le tuple
     */
    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(this.tuple);
        } catch (Exception e) {
            Logger.getGlobal().severe(e.getMessage());
        }
        return json;
    }

}
