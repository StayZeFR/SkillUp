package fr.skillup.core.bridge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.skillup.core.controller.Controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Logger;

public class Bridge {

    private static Bridge bridge = null;

    private Bridge() {
        Bridge.bridge = this;
    }

    public Object get(String controller, String method, String json) {
        Class<? extends Controller> clazz = null;
        try {
            clazz = Class.forName("fr.skillup.controllers." + controller).asSubclass(Controller.class);
            Controller instance = clazz.getConstructor().newInstance();
            ObjectMapper mapper = new ObjectMapper();
            List<Object> data = mapper.readValue(json, new TypeReference<>() {});
            if (data.isEmpty()) {
                return clazz.getMethod(method).invoke(instance);
            } else {
                Method m = this.getMethod(clazz, method);
                if (m != null) {
                    Object[] args = data.toArray();
                    return m.invoke(instance, args);
                } else {
                    Logger.getLogger(Bridge.class.getName()).severe("Method not found");
                }
            }
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException | JsonProcessingException e) {
            Logger.getLogger(Bridge.class.getName()).severe(e.getMessage());
        }
        return null;
    }

    public void call(String controller, String method, String json) {
        Class<? extends Controller> clazz = null;
        try {
            clazz = Class.forName("fr.skillup.controllers." + controller).asSubclass(Controller.class);
            Controller instance = clazz.getConstructor().newInstance();
            ObjectMapper mapper = new ObjectMapper();
            List<Object> data = mapper.readValue(json, new TypeReference<>() {});
            if (data.isEmpty()) {
                clazz.getMethod(method).invoke(instance);
            } else {
                Method m = this.getMethod(clazz, method);
                if (m != null) {
                    Object[] args = data.toArray();
                    m.invoke(instance, args);
                } else {
                    Logger.getLogger(Bridge.class.getName()).severe("Method not found");
                }
            }
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException | JsonProcessingException e) {
            Logger.getLogger(Bridge.class.getName()).severe(e.getMessage());
        }
    }

    private Method getMethod(Class<? extends Controller> clazz, String method) {
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(method)) {
                return m;
            }
        }
        return null;
    }


    public void log(String message) {
        Logger.getGlobal().info(message);
    }

    public static Bridge getInstance() {
        if (Bridge.bridge == null) {
            new Bridge();
        }
        return Bridge.bridge;
    }
}
