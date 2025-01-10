module fr.skillup.skillup {
    requires javafx.fxml;
    requires javafx.web;
    requires com.fasterxml.jackson.databind;
    requires java.sql;


    opens fr.skillup.controllers to javafx.fxml;
    opens fr.skillup.controllers.layouts to javafx.fxml;
    opens fr.skillup.core to javafx.fxml;
    opens fr.skillup.core.annotations to javafx.fxml;
    exports fr.skillup.controllers;
    exports fr.skillup.controllers.layouts;
    exports fr.skillup.core;
    exports fr.skillup.core.annotations;
}