package me.mupu.fertigungsstrasseSteuermodul.steuereinheiten;

import me.mupu.FertigungsstrasseHLD;
import me.mupu.enums.motorbewegungen.EMotorbewegungYAchse;
import me.mupu.enums.motorbewegungen.EMotorbewegungZAchse;
import me.mupu.enums.motorbewegungen.EMotorstatus;
import me.mupu.enums.sensoren.ESensorYAchse;
import me.mupu.enums.sensoren.ESensorZAchse;
import me.mupu.enums.sensoren.ESensorstatus;
import me.mupu.interfaces.maschinen.IMFraesmaschine;

public class SteuereinheitFraesmaschine extends Thread {
    private IMFraesmaschine fraesmaschine;
    private boolean terminate;
    private long time;

    public SteuereinheitFraesmaschine(IMFraesmaschine fraesmaschine) {
        this.fraesmaschine = fraesmaschine;
    }

    @Override
    public void run() {
        terminate = false;

        int zustand = 0;
        while (!terminate) {
            // Euer Code hier
            switch (zustand) {
                case 0:
                    // Ausschalten des Hubs, wenn oben
                    if (fraesmaschine.getPositionHubF() == ESensorZAchse.OBEN) {
                        fraesmaschine.setMotorstatusHubF(EMotorbewegungZAchse.AUS);
                    }
                    // Ausschalten des Querschlittens, wenn hinten
                    if (fraesmaschine.getPositionQuerschlittenF() == ESensorYAchse.HINTEN) {
                        fraesmaschine.setMotorstatusQuerschlittenF(EMotorbewegungYAchse.AUS);
                    }
                    if (fraesmaschine.getPositionHubF() == ESensorZAchse.OBEN &&
                            fraesmaschine.getPositionQuerschlittenF() == ESensorYAchse.HINTEN) {
                        zustand++;
                    } else {
                        //Herstellung der Ausgangsposition
                        // wenn Hubeinheit unten oder dazwischen
                        if (fraesmaschine.getPositionHubF() != ESensorZAchse.OBEN) {
                            fraesmaschine.setMotorstatusHubF(EMotorbewegungZAchse.AUF);
                        }
                        //wenn Querschlitten nicht oben
                        if (fraesmaschine.getPositionQuerschlittenF() != ESensorYAchse.HINTEN
                                && fraesmaschine.getPositionHubF() == ESensorZAchse.OBEN) {
                            fraesmaschine.setMotorstatusQuerschlittenF(EMotorbewegungYAchse.ZURUECK);
                        }
                    }
                    break;
                case 1:
//                    FertigungsstrasseHLD.getMehrspindelmaschine().setFlagWillWerkstueckAbgebenM(true); // DEBUG
                    if (fraesmaschine.sollWerkstueckAnnehmenF() == ESensorstatus.SIGNAL) {
                        fraesmaschine.setMotorstatusBandF(EMotorstatus.AN);

                    }
                    if (fraesmaschine.initiatorF() == ESensorstatus.SIGNAL) {
                        fraesmaschine.setMotorstatusBandF(EMotorstatus.AUS);
//                        FertigungsstrasseHLD.getMehrspindelmaschine().setFlagWillWerkstueckAbgebenM(false); // DEBUG
                        zustand++;
                    }

                    break;
                case 2:
                    if (fraesmaschine.getPositionHubF() == ESensorZAchse.OBEN &&
                            fraesmaschine.getPositionQuerschlittenF() == ESensorYAchse.HINTEN && fraesmaschine.initiatorF() == ESensorstatus.SIGNAL) { // ist Werkstück unter der Maschiene?????????????????????????????????????????????) {
                        fraesmaschine.setMotorstatusQuerschlittenF(EMotorbewegungYAchse.VOR);
                        zustand++;
                    }
                    break;
                case 3:
                    if (fraesmaschine.getPositionQuerschlittenF() == ESensorYAchse.VORNE) {
                        fraesmaschine.setMotorstatusQuerschlittenF(EMotorbewegungYAchse.AUS);
                        fraesmaschine.setMotorstatusHubF(EMotorbewegungZAchse.AB);
                        zustand++;
                    }
                    break;
                case 4:
                    if (fraesmaschine.getPositionHubF() == ESensorZAchse.UNTEN) {
                        fraesmaschine.setMotorstatusHubF(EMotorbewegungZAchse.AUS);
                        fraesmaschine.setMotorstatusWerkzeugAntriebF(EMotorstatus.AN);
                        this.time = System.currentTimeMillis();
                        zustand++;
                    }
                    break;
                case 5:
                    if (time + 7000 <= System.currentTimeMillis()) {
                        fraesmaschine.setMotorstatusWerkzeugAntriebF(EMotorstatus.AUS);
                        fraesmaschine.setMotorstatusHubF(EMotorbewegungZAchse.AUF);
                        zustand++;
                    }
                    break;
                case 6:
                    if (fraesmaschine.getPositionHubF() == ESensorZAchse.OBEN) {
                        fraesmaschine.setMotorstatusHubF(EMotorbewegungZAchse.AUS);
                        fraesmaschine.setMotorstatusQuerschlittenF(EMotorbewegungYAchse.ZURUECK);
                        zustand++;
                    }
                    break;
                case 7:
                    if (fraesmaschine.getPositionQuerschlittenF() == ESensorYAchse.HINTEN) {
                        fraesmaschine.setMotorstatusQuerschlittenF(EMotorbewegungYAchse.AUS);
                        zustand++;
                    }
                    break;
                case 8:
                    if (fraesmaschine.istAusschleussbahnBelegtF() == ESensorstatus.KEIN_SIGNAL) {
                        fraesmaschine.setMotorstatusBandF(EMotorstatus.AN);
                    }
                    if (fraesmaschine.initiatorF() == ESensorstatus.KEIN_SIGNAL &&
                            fraesmaschine.istAusschleussbahnBelegtF() == ESensorstatus.SIGNAL) {
                        fraesmaschine.setMotorstatusBandF(EMotorstatus.AUS);
                        zustand = 0;
                    }
                    break;
                //Sophie probiert das werkstück abzugeben (wird ein fail) im notfall einfach löschen

            }
            // werkstück abgeben
            // werkstück annehmen

        }
    }

    public void terminate() {
        terminate = true;
    }
}
