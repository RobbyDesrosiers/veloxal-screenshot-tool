module com.quickshot.quickshot {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.quickshot.quickshot to javafx.fxml;
    exports com.quickshot.quickshot;
}