package com.doc.semi_compiler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(MainApplication.class.getResource("sample.fxml"));

        Scene scene = new Scene(root);


        scene.getStylesheets().add(getClass().getResource("css/application.css").toExternalForm());


        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


