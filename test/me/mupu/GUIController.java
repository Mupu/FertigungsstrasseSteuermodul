package me.mupu;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import me.mupu.enums.motorbewegungen.EMotorbewegungXAchse;
import me.mupu.enums.motorbewegungen.EMotorbewegungYAchse;
import me.mupu.enums.motorbewegungen.EMotorbewegungZAchse;
import me.mupu.enums.motorbewegungen.EMotorstatus;
import me.mupu.enums.sensoren.ESensorXAchse;
import me.mupu.enums.sensoren.ESensorYAchse;
import me.mupu.enums.sensoren.ESensorZAchse;
import me.mupu.enums.sensoren.ESensorstatus;
import me.mupu.interfaces.maschinen.*;

import java.net.URL;
import java.util.ResourceBundle;


public class GUIController implements Initializable {

    @FXML
    private Label ImpulsX;

    @FXML
    private Label ImpulsY;

    @FXML
    private Label ImpulsZ;

    @FXML
    private Button ResetX;

    @FXML
    private Button ResetY;

    @FXML
    private Button ResetZ;

    @FXML
    private Button XRechts;

    @FXML
    private Button XLinks;

    @FXML
    private Button YVor;

    @FXML
    private Button YRueck;

    @FXML
    private Button ZHoch;

    @FXML
    private Button ZRunter;

    @FXML
    private CheckBox Magnet;

    @FXML
    private Button SRechts;

    @FXML
    private Button SLinks;

    @FXML
    private CheckBox SWWA;

    @FXML
    private Button BHoch;

    @FXML
    private Button BRunter;

    @FXML
    private Button BBand;

    @FXML
    private CheckBox BSWA;

    @FXML
    private CheckBox BWWA;

    @FXML
    private Button BWerkzeugantrieb;

    @FXML
    private CheckBox MSWA;

    @FXML
    private CheckBox MWWA;

    @FXML
    private Button MHoch;

    @FXML
    private Button MRunter;

    @FXML
    private Button MWerkzeugantrieb;

    @FXML
    private Button MBand;

    @FXML
    private Button MRevolverdrehung;

    @FXML
    private Button FBand;

    @FXML
    private Button FHoch;

    @FXML
    private Button FRunter;

    @FXML
    private Button FVor;

    @FXML
    private Button FRueck;

    @FXML
    private Button FWerkzeugantrieb;

    @FXML
    private CheckBox FSWA;

    @FXML
    private CheckBox I_01;

    @FXML
    private CheckBox I_02;

    @FXML
    private CheckBox I_03;

    @FXML
    private CheckBox I_04;

    @FXML
    private CheckBox I_05;

    @FXML
    private CheckBox I_06;

    @FXML
    private CheckBox I_07;

    @FXML
    private CheckBox I_08;

    @FXML
    private CheckBox I_09;

    @FXML
    private CheckBox I_10;

    @FXML
    private CheckBox I_11;

    @FXML
    private CheckBox I_12;

    @FXML
    private CheckBox I_13;

    @FXML
    private CheckBox I_14;

    @FXML
    private CheckBox I_17;

    @FXML
    private CheckBox I_18;

    @FXML
    private CheckBox I_19;

    @FXML
    private CheckBox I_20;

    @FXML
    private CheckBox I_21;

    @FXML
    private CheckBox I_22;

    @FXML
    private CheckBox I_25;

    @FXML
    private CheckBox I_26;

    @FXML
    private CheckBox I_27;

    @FXML
    private CheckBox I_28;

    @FXML
    private CheckBox I_29;

    @FXML
    private CheckBox I_30;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FertigungsstrasseHLD.open();

        IKran k = FertigungsstrasseHLD.getKran();
        IMSchieber s = FertigungsstrasseHLD.getSchieber();
        IMBohrmaschine b = FertigungsstrasseHLD.getBohrmaschine();
        IMMehrspindelmaschine m = FertigungsstrasseHLD.getMehrspindelmaschine();
        IMFraesmaschine f = FertigungsstrasseHLD.getFraesmaschine();

        // Kran
        // Reset Buttons
        ResetX.setOnMouseClicked((e) -> ImpulsX.setText("0"));
        ResetY.setOnMouseClicked((e) -> ImpulsY.setText("0"));
        ResetZ.setOnMouseClicked((e) -> ImpulsZ.setText("0"));
        // X
        XLinks.setOnMousePressed((e) -> k.setMotorstatusXAchseK(EMotorbewegungXAchse.LINKS));
        XLinks.setOnMouseReleased((e) -> k.setMotorstatusXAchseK(EMotorbewegungXAchse.AUS));

        XRechts.setOnMousePressed((e) -> k.setMotorstatusXAchseK(EMotorbewegungXAchse.RECHTS));
        XRechts.setOnMouseReleased((e) -> k.setMotorstatusXAchseK(EMotorbewegungXAchse.AUS));
        // Y
        YVor.setOnMousePressed((e) -> k.setMotorstatusYAchseK(EMotorbewegungYAchse.VOR));
        YVor.setOnMouseReleased((e) -> k.setMotorstatusYAchseK(EMotorbewegungYAchse.AUS));

        YRueck.setOnMousePressed((e) -> k.setMotorstatusYAchseK(EMotorbewegungYAchse.ZURUECK));
        YRueck.setOnMouseReleased((e) -> k.setMotorstatusYAchseK(EMotorbewegungYAchse.AUS));
        // Z
        ZHoch.setOnMousePressed((e) -> k.setMotorstatusZAchseK(EMotorbewegungZAchse.AUF));
        ZHoch.setOnMouseReleased((e) -> k.setMotorstatusZAchseK(EMotorbewegungZAchse.AUS));

        ZRunter.setOnMousePressed((e) -> k.setMotorstatusZAchseK(EMotorbewegungZAchse.AB));
        ZRunter.setOnMouseReleased((e) -> k.setMotorstatusZAchseK(EMotorbewegungZAchse.AUS));
        // Magnet
        Magnet.selectedProperty().addListener((observable, oldValue, newValue) -> k.setMotorstatusMagnetK(newValue ? EMotorstatus.AN : EMotorstatus.AUS));


        // Schieber
        // Links
        SLinks.setOnMousePressed((e) -> s.setMotorstatusSchieberS(EMotorbewegungXAchse.LINKS));
        SLinks.setOnMouseReleased((e) -> s.setMotorstatusSchieberS(EMotorbewegungXAchse.AUS));
        // Rechts
        SRechts.setOnMousePressed((e) -> s.setMotorstatusSchieberS(EMotorbewegungXAchse.RECHTS));
        SRechts.setOnMouseReleased((e) -> s.setMotorstatusSchieberS(EMotorbewegungXAchse.AUS));
        // WWA
        SWWA.selectedProperty().addListener((observable, oldValue, newValue) -> s.setFlagWillWerkstueckAbgebenS(newValue));


        // Bohrmaschine
        // Hoch
        BHoch.setOnMousePressed((e) -> b.setMotorstatusHubB(EMotorbewegungZAchse.AUF));
        BHoch.setOnMouseReleased((e) -> b.setMotorstatusHubB(EMotorbewegungZAchse.AUS));
        // Runter
        BRunter.setOnMousePressed((e) -> b.setMotorstatusHubB(EMotorbewegungZAchse.AB));
        BRunter.setOnMouseReleased((e) -> b.setMotorstatusHubB(EMotorbewegungZAchse.AUS));
        // Werkzeugantrieb
        BWerkzeugantrieb.setOnMousePressed((e) -> b.setMotorstatusWerkzeugAntriebB(EMotorstatus.AN));
        BWerkzeugantrieb.setOnMouseReleased((e) -> b.setMotorstatusWerkzeugAntriebB(EMotorstatus.AUS));
        // Band
        BBand.setOnMousePressed((e) -> b.setMotorstatusBandB(EMotorstatus.AN));
        BBand.setOnMouseReleased((e) -> b.setMotorstatusBandB(EMotorstatus.AUS));
        // WWA
        BWWA.selectedProperty().addListener((observable, oldValue, newValue) -> b.setFlagWillWerkstueckAbgebenB(newValue));


        // Mehrspindelmaschine
        // Hoch
        MHoch.setOnMousePressed((e) -> m.setMotorstatusHubM(EMotorbewegungZAchse.AUF));
        MHoch.setOnMouseReleased((e) -> m.setMotorstatusHubM(EMotorbewegungZAchse.AUS));
        // Runter
        MRunter.setOnMousePressed((e) -> m.setMotorstatusHubM(EMotorbewegungZAchse.AB));
        MRunter.setOnMouseReleased((e) -> m.setMotorstatusHubM(EMotorbewegungZAchse.AUS));
        // Werkzeugantrieb
        MWerkzeugantrieb.setOnMousePressed((e) -> m.setMotorstatusWerkzeugAntriebM(EMotorstatus.AN));
        MWerkzeugantrieb.setOnMouseReleased((e) -> m.setMotorstatusWerkzeugAntriebM(EMotorstatus.AUS));
        // Revolverantrieb
        MRevolverdrehung.setOnMousePressed((e) -> m.setMotorstatusRevolverdrehungM(EMotorstatus.AN));
        MRevolverdrehung.setOnMouseReleased((e) -> m.setMotorstatusRevolverdrehungM(EMotorstatus.AUS));
        // Band
        MBand.setOnMousePressed((e) -> m.setMotorstatusBandM(EMotorstatus.AN));
        MBand.setOnMouseReleased((e) -> m.setMotorstatusBandM(EMotorstatus.AUS));
        // WWA
        MWWA.selectedProperty().addListener((observable, oldValue, newValue) -> m.setFlagWillWerkstueckAbgebenM(newValue));


        // Fraesmaschine
        // Hoch
        FHoch.setOnMousePressed((e) -> f.setMotorstatusHubF(EMotorbewegungZAchse.AUF));
        FHoch.setOnMouseReleased((e) -> f.setMotorstatusHubF(EMotorbewegungZAchse.AUS));
        // Runter
        FRunter.setOnMousePressed((e) -> f.setMotorstatusHubF(EMotorbewegungZAchse.AB));
        FRunter.setOnMouseReleased((e) -> f.setMotorstatusHubF(EMotorbewegungZAchse.AUS));
        // Vor
        FVor.setOnMousePressed((e) -> f.setMotorstatusQuerschlittenF(EMotorbewegungYAchse.VOR));
        FVor.setOnMouseReleased((e) -> f.setMotorstatusQuerschlittenF(EMotorbewegungYAchse.AUS));
        // Rueck
        FRueck.setOnMousePressed((e) -> f.setMotorstatusQuerschlittenF(EMotorbewegungYAchse.ZURUECK));
        FRueck.setOnMouseReleased((e) -> f.setMotorstatusQuerschlittenF(EMotorbewegungYAchse.AUS));
        // Werkzeugantrieb
        FWerkzeugantrieb.setOnMousePressed((e) -> f.setMotorstatusWerkzeugAntriebF(EMotorstatus.AN));
        FWerkzeugantrieb.setOnMouseReleased((e) -> f.setMotorstatusWerkzeugAntriebF(EMotorstatus.AUS));
        // Band
        FBand.setOnMousePressed((e) -> f.setMotorstatusBandF(EMotorstatus.AN));
        FBand.setOnMouseReleased((e) -> f.setMotorstatusBandF(EMotorstatus.AUS));


        // init X
        new Thread(() -> {
            ESensorstatus x = k.initiatorXAchseK();
            while (true) {
                while (k.initiatorXAchseK() == x);
                Platform.runLater(() -> ImpulsX.setText(String.valueOf(Integer.valueOf(ImpulsX.getText()) + 1)));
                while (k.initiatorXAchseK() != x);
            }
        }).start();

        // init Z
        new Thread(() -> {
            ESensorstatus y = k.initiatorYAchseK();
            while (true) {
                while (k.initiatorYAchseK() == y);
                Platform.runLater(() -> ImpulsY.setText(String.valueOf(Integer.valueOf(ImpulsY.getText()) + 1)));
                while (k.initiatorYAchseK() != y);
            }
        }).start();

        // init Y
        new Thread(() -> {
            ESensorstatus z = k.initiatorZAchseK();
            while (true) {
                while (k.initiatorZAchseK() == z);
                Platform.runLater(() -> ImpulsZ.setText(String.valueOf(Integer.valueOf(ImpulsZ.getText()) + 1)));
                while (k.initiatorZAchseK() != z);
            }
        }).start();




        new Thread(() -> {
            boolean bool = SWWA.selectedProperty().get();
            while (true) {
                while (SWWA.selectedProperty().get() == bool);
                Platform.runLater(() -> BSWA.selectedProperty().setValue(!SWWA.selectedProperty().get()));
                while (SWWA.selectedProperty().get() != bool);
            }
        }).start();

        new Thread(() -> {
            boolean bool = BWWA.selectedProperty().get();
            while (true) {
                while (BWWA.selectedProperty().get() == bool);
                Platform.runLater(() -> MSWA.selectedProperty().setValue(!BWWA.selectedProperty().get()));
                while (BWWA.selectedProperty().get() != bool);
            }
        }).start();

        new Thread(() -> {
            boolean bool = MWWA.selectedProperty().get();
            while (true) {
                while (MWWA.selectedProperty().get() == bool);
                Platform.runLater(() -> FSWA.selectedProperty().setValue(!MWWA.selectedProperty().get()));
                while (MWWA.selectedProperty().get() != bool);
            }
        }).start();





        // daten auslesen
        new Thread(() -> {
            while (true) {
                try {
                    I_01.selectedProperty().setValue(s.istEinlegestationBelegtS() == ESensorstatus.SIGNAL);

                    ESensorXAchse schieberS = s.getPositionSchieberS();
                    I_02.selectedProperty().setValue(schieberS == ESensorXAchse.LINKS);
                    I_03.selectedProperty().setValue(schieberS == ESensorXAchse.RECHTS);

                    ESensorZAchse hubB = b.getPositionHubB();
                    I_04.selectedProperty().setValue(hubB == ESensorZAchse.OBEN);
                    I_05.selectedProperty().setValue(hubB == ESensorZAchse.UNTEN);

                    ESensorZAchse hubM = m.getPositionHubM();
                    I_06.selectedProperty().setValue(hubM == ESensorZAchse.OBEN);
                    I_07.selectedProperty().setValue(hubM == ESensorZAchse.UNTEN);

                    I_08.selectedProperty().setValue(m.istRevolverAufPositionM() == ESensorstatus.SIGNAL);

                    ESensorZAchse hubF = f.getPositionHubF();
                    I_09.selectedProperty().setValue(hubF == ESensorZAchse.OBEN);
                    I_10.selectedProperty().setValue(hubF == ESensorZAchse.UNTEN);

                    ESensorYAchse querschlittenF = f.getPositionQuerschlittenF();
                    I_11.selectedProperty().setValue(querschlittenF == ESensorYAchse.VORNE);
                    I_12.selectedProperty().setValue(querschlittenF == ESensorYAchse.HINTEN);

                    I_13.selectedProperty().setValue(s.istUebergabestelleVorBohrmaschineBelegtS() == ESensorstatus.SIGNAL);

                    I_14.selectedProperty().setValue(f.istAusschleussbahnBelegtF() == ESensorstatus.SIGNAL);

                    ESensorXAchse xPosK = k.getPositionXAchseK();
                    I_17.selectedProperty().setValue(xPosK == ESensorXAchse.RECHTS);
                    I_18.selectedProperty().setValue(xPosK == ESensorXAchse.LINKS);

                    ESensorYAchse yPosK = k.getPositionYAchseK();
                    I_19.selectedProperty().setValue(yPosK == ESensorYAchse.VORNE);
                    I_20.selectedProperty().setValue(yPosK == ESensorYAchse.HINTEN);

                    ESensorZAchse zPosK = k.getPositionZAchseK();
                    I_21.selectedProperty().setValue(zPosK == ESensorZAchse.OBEN);
                    I_22.selectedProperty().setValue(zPosK == ESensorZAchse.UNTEN);

                    I_25.selectedProperty().setValue(b.initiatorB() == ESensorstatus.SIGNAL);

                    I_26.selectedProperty().setValue(m.initiatorM() == ESensorstatus.SIGNAL);

                    I_27.selectedProperty().setValue(f.initiatorF() == ESensorstatus.SIGNAL);

                    I_28.selectedProperty().setValue(k.initiatorXAchseK() == ESensorstatus.SIGNAL);

                    I_29.selectedProperty().setValue(k.initiatorYAchseK() == ESensorstatus.SIGNAL);

                    I_30.selectedProperty().setValue(k.initiatorZAchseK() == ESensorstatus.SIGNAL);

                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }).start();
    }
}
