module com.desrosiers.veloxal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.desrosiersrobby.veloxal to javafx.fxml;
    exports com.desrosiersrobby.veloxal;
    exports com.desrosiersrobby.veloxal.controllers;
    opens com.desrosiersrobby.veloxal.controllers to javafx.fxml;
    exports com.desrosiersrobby.veloxal.utilities;
    opens com.desrosiersrobby.veloxal.utilities to javafx.fxml;
    exports com.desrosiersrobby.veloxal.ui;
    opens com.desrosiersrobby.veloxal.ui to javafx.fxml;
    exports com.desrosiersrobby.veloxal.ui.abstracts;
    opens com.desrosiersrobby.veloxal.ui.abstracts to javafx.fxml;
}