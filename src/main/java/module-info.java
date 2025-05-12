module org.example.pecl_candelasanz_maria {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires java.rmi;

    opens org.example.pecl_candelasanz_maria.Parte2 to javafx.fxml;
    exports org.example.pecl_candelasanz_maria.Parte2;
    exports org.example.pecl_candelasanz_maria.Parte1;
    opens org.example.pecl_candelasanz_maria.Parte1 to javafx.fxml;
}