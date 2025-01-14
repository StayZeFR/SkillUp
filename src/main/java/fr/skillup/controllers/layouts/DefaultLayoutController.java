package fr.skillup.controllers.layouts;

import fr.skillup.controllers.HomeController;
import fr.skillup.controllers.MissionsController;
import fr.skillup.core.controller.Controller;
import fr.skillup.core.controller.LayoutController;

import java.util.HashMap;
import java.util.Map;

public class DefaultLayoutController extends LayoutController {
    public void moveTo(String view) {
        Map<String, Object> params = new HashMap<>();
        params.put("view", view);
        Class<? extends Controller> controller = switch (view) {
            case "home" -> HomeController.class;
            case "missions" -> MissionsController.class;
            default -> throw new IllegalStateException("Unexpected value: " + view);
        };
        this.window.show(controller, params);
    }
}
