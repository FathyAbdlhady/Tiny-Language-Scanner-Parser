module com.doc.semi_compiler {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires guru.nidi.graphviz;

    opens com.doc.semi_compiler to javafx.fxml, javafx.graphics, guru.nidi.graphviz;
    exports com.doc.semi_compiler;

    exports com.doc.semi_compiler.controller;
    opens com.doc.semi_compiler.controller to javafx.fxml, javafx.graphics, guru.nidi.graphviz;
}