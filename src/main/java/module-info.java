module com.quickshot.quickshot {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.quickshot.quickshot to javafx.fxml;
    exports com.quickshot.quickshot;
    exports com.quickshot.quickshot.controllers;
    opens com.quickshot.quickshot.controllers to javafx.fxml;
    exports com.quickshot.quickshot.utilities;
    opens com.quickshot.quickshot.utilities to javafx.fxml;
    exports com.quickshot.quickshot.ui;
    opens com.quickshot.quickshot.ui to javafx.fxml;
    exports com.quickshot.quickshot.ui.abstracts;
    opens com.quickshot.quickshot.ui.abstracts to javafx.fxml;
}