package jn.rocbot_control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Login extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(
                FXMLLoader.load(getClass().getResource("/jn.rocbot_control/Roc-bot_login.fxml")))
        ); primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

