package me.mupu.fertigungsstrasseSteuermodul.UI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import me.mupu.FertigungsstrasseHLD;
import me.mupu.fertigungsstrasseSteuermodul.Steuermodul;

import java.net.URL;
import java.util.ResourceBundle;

public class Finylllllllay_GuiWuiWengControlla implements Initializable {

    @FXML
    private Button start;

    @FXML
    private Button stop;

    private Steuermodul steuermodul;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        start.setOnMouseClicked(e -> {
            steuermodul = new Steuermodul();
            steuermodul.start();
        });

        stop.setOnMouseClicked(e -> {
            steuermodul.stop();
        });
    }
}
