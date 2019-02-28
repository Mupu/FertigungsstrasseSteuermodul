package me.mupu.fertigungsstrasseSteuermodul.UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.mupu.FertigungsstrasseHLD;

import java.io.IOException;

public class FINALYFNIALY_GUIMUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/me/mupu/fertigungsstrasseSteuermodul/UI/FinalyFinalyFinalyGuyMui.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setOnCloseRequest(e -> {
            FertigungsstrasseHLD.close();
            System.exit(0);
        });

        primaryStage.setTitle("FertigungsstrasseHDL-UI");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
