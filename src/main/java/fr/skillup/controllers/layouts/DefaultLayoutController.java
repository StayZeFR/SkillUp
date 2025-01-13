package fr.skillup.controllers.layouts;

import fr.skillup.controllers.HomeController;
import fr.skillup.controllers.MissionsController;
import fr.skillup.core.controller.LayoutController;

public class DefaultLayoutController extends LayoutController {
    public void moveTo(String view) {
        switch (view) {
            case "home" -> super.window.show(HomeController.class);
            case "missions" -> super.window.show(MissionsController.class);
        }
    }
}
