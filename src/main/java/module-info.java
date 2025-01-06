module fr.skillup.skillup {
    requires javafx.controls;
    requires javafx.fxml;


    opens fr.skillup.skillup to javafx.fxml;
    exports fr.skillup.skillup;
}