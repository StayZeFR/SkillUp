module fr.skillup {
    requires javafx.fxml;
    requires javafx.web;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires jdk.jsobject;
    requires jdk.xml.dom;
    requires org.jsoup;
    requires jcef;
    requires javafx.swing;


    opens fr.skillup to javafx.fxml;
    opens fr.skillup.core to javafx.fxml;
    opens fr.skillup.controllers to javafx.fxml;
    opens fr.skillup.controllers.layouts to javafx.fxml;
    opens fr.skillup.core.controller to javafx.fxml;
    opens fr.skillup.core.window to javafx.fxml;
    opens fr.skillup.core.bridge to javafx.fxml;
    exports fr.skillup;
    exports fr.skillup.core;
    exports fr.skillup.controllers;
    exports fr.skillup.controllers.layouts;
    exports fr.skillup.core.controller;
    exports fr.skillup.core.window;
    exports fr.skillup.core.bridge;
}