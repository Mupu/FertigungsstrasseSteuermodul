package me.mupu.fertigungsstrasseSteuermodul.UI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import me.mupu.FertigungsstrasseHLD;
import me.mupu.enums.sensoren.ESensorXAchse;
import me.mupu.enums.sensoren.ESensorYAchse;
import me.mupu.enums.sensoren.ESensorZAchse;
import me.mupu.enums.sensoren.ESensorstatus;
import me.mupu.fertigungsstrasseSteuermodul.Steuermodul;
import me.mupu.interfaces.maschinen.*;

import java.net.URL;
import java.util.ResourceBundle;

public class HauptController implements Initializable {

    @FXML
    private ImageView startButton;
    @FXML
    private ImageView I1;
    @FXML
    private ImageView I2;
    @FXML
    private ImageView I3;
    @FXML
    private ImageView I4;
    @FXML
    private ImageView I5;
    @FXML
    private ImageView I6;
    @FXML
    private ImageView I7;
    @FXML
    private ImageView I8;
    @FXML
    private ImageView I9;
    @FXML
    private ImageView I10;
    @FXML
    private ImageView I11;
    @FXML
    private ImageView I12;
    @FXML
    private ImageView I13;
    @FXML
    private ImageView I14;
    @FXML
    private ImageView I17;
    @FXML
    private ImageView I18;
    @FXML
    private ImageView I19;
    @FXML
    private ImageView I20;
    @FXML
    private ImageView I21;
    @FXML
    private ImageView I22;
    @FXML
    private ImageView I25;
    @FXML
    private ImageView I26;
    @FXML
    private ImageView I27;
    @FXML
    private ImageView I28;
    @FXML
    private ImageView I29;
    @FXML
    private ImageView I30;
    @FXML
    private ImageView startLampe;
    @FXML
    private ImageView WerkStückEins;
    @FXML
    private ImageView WerkStückZwei;
    @FXML
    private ImageView WerkStückDrei;
    @FXML
    private ImageView WerkStückEnde;
    @FXML
    private ImageView WerkStückStart;
    @FXML
    private ImageView KalibrierenButton;

    private Steuermodul steuermodul;

    private boolean I1bo;
    private boolean I2bo;
    private boolean I3bo;
    private boolean I4bo;
    private boolean I5bo;
    private boolean I6bo;
    private boolean I7bo;
    private boolean I8bo;
    private boolean I9bo;
    private boolean I10bo;
    private boolean I11bo;
    private boolean I12bo;
    private boolean I13bo;
    private boolean I14bo;
    private boolean I17bo;
    private boolean I18bo;
    private boolean I19bo;
    private boolean I20bo;
    private boolean I21bo;
    private boolean I22bo;
    private boolean I25bo;
    private boolean I26bo;
    private boolean I27bo;
    private boolean I28bo;
    private boolean I29bo;
    private boolean I30bo;
    private boolean machineAN;
    private Thread leseThread;
    private int werkStückplatz;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        steuermodul = new Steuermodul();
    }

    private void lampenchanger(ImageView image, boolean ON) {
        if (ON) {
            image.setImage(new Image("/me/mupu/fertigungsstrasseSteuermodul/UI/ressourcen/lampeAn.png"));
        } else {
            image.setImage(new Image("/me/mupu/fertigungsstrasseSteuermodul/UI/ressourcen/lampeAus.png"));
        }
    }

    public void startStop() {
        if (machineAN) {        // STOP
            startButton.setImage(new Image("/me/mupu/fertigungsstrasseSteuermodul/UI/ressourcen/startButton.png"));
            KalibrierenButton.setImage(new Image("/me/mupu/fertigungsstrasseSteuermodul/UI/ressourcen/KalibrierenButton.png"));
            steuermodul.stop();
            machineAN = false;
            lampenchanger(startLampe,false);
        } else {                //START
            startButton.setImage(new Image("/me/mupu/fertigungsstrasseSteuermodul/UI/ressourcen/stopButton.png"));
            KalibrierenButton.setImage(new Image("/me/mupu/fertigungsstrasseSteuermodul/UI/ressourcen/KalibrierenButtonAusgegraut.png"));
            steuermodul.start();
            machineAN = true;
            initializeStrasse();
            lampenchanger(startLampe, true);
        }
    }

    public void kalibrieren() {
//        if (!machineAN){                //START KALIBRIEREN
//            // TODO KALIBRIEREN HINZUFÜGEN
//            startButton.setImage(new Image("/me/mupu/fertigungsstrasseSteuermodul/UI/ressourcen/stopButton.png"));
//            KalibrierenButton.setImage(new Image("/me/mupu/fertigungsstrasseSteuermodul/UI/ressourcen/KalibrierenButtonAusgegraut.png"));
//            steuermodul.start();
//            machineAN = true;
//            initializeStrasse();
//            lampenchanger(startLampe, true);
//
//        }
    }


    private void initializeStrasse() {
        IKran k = FertigungsstrasseHLD.getKran();
        IMSchieber s = FertigungsstrasseHLD.getSchieber();
        IMBohrmaschine b = FertigungsstrasseHLD.getBohrmaschine();
        IMMehrspindelmaschine m = FertigungsstrasseHLD.getMehrspindelmaschine();
        IMFraesmaschine f = FertigungsstrasseHLD.getFraesmaschine();

        // daten auslesen
        if (machineAN) {
            leseThread = new Thread() {
                public void run() {
                    while (machineAN) {
                        try {
                            I1bo = (s.istEinlegestationBelegtS() == ESensorstatus.SIGNAL);

                            ESensorXAchse schieberS = s.getPositionSchieberS();
                            I2bo = (schieberS == ESensorXAchse.LINKS);
                            I3bo = (schieberS == ESensorXAchse.RECHTS);

                            ESensorZAchse hubB = b.getPositionHubB();
                            I4bo = (hubB == ESensorZAchse.OBEN);
                            I5bo = (hubB == ESensorZAchse.UNTEN);

                            ESensorZAchse hubM = m.getPositionHubM();
                            I6bo = (hubM == ESensorZAchse.OBEN);
                            I7bo = (hubM == ESensorZAchse.UNTEN);

                            I8bo = (m.istRevolverAufPositionM() == ESensorstatus.SIGNAL);

                            ESensorZAchse hubF = f.getPositionHubF();
                            I9bo = (hubF == ESensorZAchse.OBEN);
                            I10bo = (hubF == ESensorZAchse.UNTEN);

                            ESensorYAchse querschlittenF = f.getPositionQuerschlittenF();
                            I11bo = (querschlittenF == ESensorYAchse.VORNE);
                            I12bo = (querschlittenF == ESensorYAchse.HINTEN);

                            I13bo = (s.istUebergabestelleVorBohrmaschineBelegtS() == ESensorstatus.SIGNAL);

                            I14bo = (f.istAusschleussbahnBelegtF() == ESensorstatus.SIGNAL);

                            ESensorXAchse xPosK = k.getPositionXAchseK();
                            I17bo = (xPosK == ESensorXAchse.RECHTS);
                            I18bo = (xPosK == ESensorXAchse.LINKS);

                            ESensorYAchse yPosK = k.getPositionYAchseK();
                            I19bo = (yPosK == ESensorYAchse.VORNE);
                            I20bo = (yPosK == ESensorYAchse.HINTEN);

                            ESensorZAchse zPosK = k.getPositionZAchseK();
                            I21bo = (zPosK == ESensorZAchse.OBEN);
                            I22bo = (zPosK == ESensorZAchse.UNTEN);

                            I25bo = (b.initiatorB() == ESensorstatus.SIGNAL);

                            I26bo = (m.initiatorM() == ESensorstatus.SIGNAL);

                            I27bo = (f.initiatorF() == ESensorstatus.SIGNAL);

                            I28bo = (k.initiatorXAchseK() == ESensorstatus.SIGNAL);

                            I29bo = (k.initiatorYAchseK() == ESensorstatus.SIGNAL);

                            I30bo = (k.initiatorZAchseK() == ESensorstatus.SIGNAL);

                            if (I1bo) {
                                lampenchanger(I1, true);
                                WerkStückStart.setVisible(true);
                            } else {
                                lampenchanger(I1, false);
                                WerkStückStart.setVisible(false);
                            }
                            if (I2bo) {
                                lampenchanger(I2, true);
                            } else {
                                lampenchanger(I2, false);
                            }
                            if (I3bo) {
                                lampenchanger(I3, true);
                            } else {
                                lampenchanger(I3, false);
                            }
                            if (I4bo) {
                                lampenchanger(I4, true);
                            } else {
                                lampenchanger(I4, false);
                            }
                            if (I5bo) {
                                lampenchanger(I5, true);
                            } else {
                                lampenchanger(I5, false);
                            }
                            if (I6bo) {
                                lampenchanger(I6, true);
                            } else {
                                lampenchanger(I6, false);
                            }
                            if (I7bo) {
                                lampenchanger(I7, true);
                            } else {
                                lampenchanger(I7, false);
                            }
                            if (I8bo) {
                                lampenchanger(I8, true);
                            } else {
                                lampenchanger(I8, false);
                            }
                            if (I9bo) {
                                lampenchanger(I9, true);
                            } else {
                                lampenchanger(I9, false);
                            }
                            if (I10bo) {
                                lampenchanger(I10, true);
                            } else {
                                lampenchanger(I10, false);
                            }
                            if (I11bo) {
                                lampenchanger(I11, true);
                            } else {
                                lampenchanger(I11, false);
                            }
                            if (I12bo) {
                                lampenchanger(I12, true);
                            } else {
                                lampenchanger(I12, false);
                            }
                            if (I13bo) {
                                lampenchanger(I13, true);
                            } else {
                                lampenchanger(I13, false);
                            }
                            if (I14bo) {
                                lampenchanger(I14, true);
                                WerkStückEnde.setVisible(true);
                            } else {
                                lampenchanger(I14, false);
                                WerkStückEnde.setVisible(false);
                            }
                            if (I17bo) {
                                lampenchanger(I17, true);
                            } else {
                                lampenchanger(I17, false);
                            }
                            if (I18bo) {
                                lampenchanger(I18, true);
                            } else {
                                lampenchanger(I18, false);
                            }
                            if (I19bo) {
                                lampenchanger(I19, true);
                            } else {
                                lampenchanger(I19, false);
                            }
                            if (I20bo) {
                                lampenchanger(I20, true);
                            } else {
                                lampenchanger(I20, false);
                            }
                            if (I21bo) {
                                lampenchanger(I21, true);
                            } else {
                                lampenchanger(I21, false);
                            }
                            if (I22bo) {
                                lampenchanger(I22, true);
                            } else {
                                lampenchanger(I22, false);
                            }
                            if (I25bo) {
                                lampenchanger(I25, true);
                                WerkStückEins.setVisible(true);
                            } else {
                                lampenchanger(I25, false);
                                WerkStückEins.setVisible(false);
                            }
                            if (I26bo) {
                                lampenchanger(I26, true);
                                WerkStückZwei.setVisible(true);
                            } else {
                                lampenchanger(I26, false);
                                WerkStückZwei.setVisible(false);
                            }
                            if (I27bo) {
                                lampenchanger(I27, true);
                                WerkStückDrei.setVisible(true);
                            } else {
                                lampenchanger(I27, false);
                                WerkStückDrei.setVisible(false);
                            }
                            if (I28bo) {
                                lampenchanger(I28, true);
                            } else {
                                lampenchanger(I28, false);
                            }
                            if (I29bo) {
                                lampenchanger(I29, true);
                            } else {
                                lampenchanger(I29, false);
                            }
                            if (I30bo) {
                                lampenchanger(I30, true);
                            } else {
                                lampenchanger(I30, false);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            System.exit(1);
                        }
                    }
                }
            };
            leseThread.start();
        } else {
//            leseThread.stop();
        }
    }
}
