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
        int zustand = 0;
        terminate = false;

        while (!terminate) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            System.out.println(fraesmaschine.sollWerkstueckAnnehmenF());
            switch (zustand) {
                case 0:
                    // Ausschalten des Hubs, wenn oben
                    if (fraesmaschine.getPositionHubF() == ESensorZAchse.OBEN) {
                        fraesmaschine.setMotorstatusHubF(EMotorbewegungZAchse.AUS);
//                        System.out.println("Ausschalten des Hubs, wenn oben");
                    }
                    // Ausschalten des Querschlittens, wenn hinten
                    if (fraesmaschine.getPositionQuerschlittenF() == ESensorYAchse.HINTEN) {
                        fraesmaschine.setMotorstatusQuerschlittenF(EMotorbewegungYAchse.AUS);
//                        System.out.println("Ausschalten des Querschlittens, wenn hinten");
                    }
                    if (fraesmaschine.getPositionHubF() == ESensorZAchse.OBEN &&
                            fraesmaschine.getPositionQuerschlittenF() == ESensorYAchse.HINTEN) {
                        zustand++;
//                        System.out.println("Ausschalten des Querschlittens, wenn hinten");
                    } else {
                        //Herstellung der Ausgangsposition
                        // wenn Hubeinheit unten oder dazwischen
                        if (fraesmaschine.getPositionHubF() != ESensorZAchse.OBEN) {
                            fraesmaschine.setMotorstatusHubF(EMotorbewegungZAchse.AUF);
//                            System.out.println("Herstellung der Ausgangsposition wenn Hubeinheit unten oder dazwischen");
                        }
                        //wenn Querschlitten nicht oben
                        if (fraesmaschine.getPositionQuerschlittenF() != ESensorYAchse.HINTEN
                                && fraesmaschine.getPositionHubF() == ESensorZAchse.OBEN) {
                            fraesmaschine.setMotorstatusQuerschlittenF(EMotorbewegungYAchse.ZURUECK);
//                            System.out.println("Herstellung der Ausgangsposition wenn Querschlitten nicht hinten");
                        }
                    }
                    break;
                case 1:
//                    FertigungsstrasseHLD.getMehrspindelmaschine().setFlagWillWerkstueckAbgebenM(true); // DEBUG
                    /*if (fraesmaschine.sollWerkstueckAnnehmenF() == ESensorstatus.SIGNAL) {
                        fraesmaschine.setMotorstatusBandF(EMotorstatus.AN);
                    }*/

                    if (fraesmaschine.sollWerkstueckAnnehmenF() == ESensorstatus.SIGNAL) {
                        fraesmaschine.setMotorstatusBandF(EMotorstatus.AN);
//                        System.out.println("aktiviere Band");
                    }
                    /*if (fraesmaschine.initiatorF() == ESensorstatus.SIGNAL) {
                        fraesmaschine.setMotorstatusBandF(EMotorstatus.AUS);
//                        FertigungsstrasseHLD.getMehrspindelmaschine().setFlagWillWerkstueckAbgebenM(false); // DEBUG
                        zustand++;
                    }
                    break;*/
                    if (fraesmaschine.initiatorF() == ESensorstatus.SIGNAL) {
                        fraesmaschine.setMotorstatusBandF(EMotorstatus.AUS);
//                        FertigungsstrasseHLD.getMehrspindelmaschine().setFlagWillWerkstueckAbgebenM(false); // DEBUG
//                        System.out.println("Band aus");
                        zustand++;
                    }
                    break;
                case 2:
                    /*if (fraesmaschine.getPositionHubF() == ESensorZAchse.OBEN &&
                            fraesmaschine.getPositionQuerschlittenF() == ESensorYAchse.HINTEN && fraesmaschine.initiatorF() == ESensorstatus.SIGNAL) { // ist Werkstück unter der Maschiene?????????????????????????????????????????????) {
                        fraesmaschine.setMotorstatusQuerschlittenF(EMotorbewegungYAchse.VOR);
                        zustand++;
                    }
                    break;*/
                    if (fraesmaschine.getPositionHubF() == ESensorZAchse.OBEN &&
                            fraesmaschine.getPositionQuerschlittenF() == ESensorYAchse.HINTEN && fraesmaschine.initiatorF() == ESensorstatus.SIGNAL) { // ist Werkstück unter der Maschiene?????????????????????????????????????????????) {
                        fraesmaschine.setMotorstatusQuerschlittenF(EMotorbewegungYAchse.VOR);
                        zustand++;
//                        System.out.println("Vor");
                    }
                    break;
                case 3:
                    if (fraesmaschine.getPositionQuerschlittenF() == ESensorYAchse.VORNE) {
                        fraesmaschine.setMotorstatusQuerschlittenF(EMotorbewegungYAchse.AUS);
                        fraesmaschine.setMotorstatusHubF(EMotorbewegungZAchse.AB);
                        zustand++;
//                        System.out.println("runter");
                    }
                    break;
                case 4:
                    if (fraesmaschine.getPositionHubF() == ESensorZAchse.UNTEN) {
                        fraesmaschine.setMotorstatusHubF(EMotorbewegungZAchse.AUS);
                        fraesmaschine.setMotorstatusWerkzeugAntriebF(EMotorstatus.AN);
                        this.time = System.currentTimeMillis();
                        zustand++;
//                        System.out.println("Antrieb aktiv");
                    }
                    break;
                case 5:
                    if (time + 7000 <= System.currentTimeMillis()) {
                        fraesmaschine.setMotorstatusWerkzeugAntriebF(EMotorstatus.AUS);
                        fraesmaschine.setMotorstatusHubF(EMotorbewegungZAchse.AUF);
                        zustand++;
//                        System.out.println("Antrieb aus und hoch");
                    }
                    break;
                case 6:
                    if (fraesmaschine.getPositionHubF() == ESensorZAchse.OBEN) {
                        fraesmaschine.setMotorstatusHubF(EMotorbewegungZAchse.AUS);
                        fraesmaschine.setMotorstatusQuerschlittenF(EMotorbewegungYAchse.ZURUECK);
                        zustand++;
//                        System.out.println("zurück");
                    }
                    break;
                case 7:
                    if (fraesmaschine.getPositionQuerschlittenF() == ESensorYAchse.HINTEN) {
                        fraesmaschine.setMotorstatusQuerschlittenF(EMotorbewegungYAchse.AUS);
                        zustand++;
//                        System.out.println("Querschlitten aus");
                    }
                    break;
                case 8:
                    /*if (fraesmaschine.istAusschleussbahnBelegtF() == ESensorstatus.KEIN_SIGNAL) {
                        fraesmaschine.setMotorstatusBandF(EMotorstatus.AN);
                    }*/
                    if (fraesmaschine.istAusschleussbahnBelegtF() == ESensorstatus.KEIN_SIGNAL) {
                        fraesmaschine.setMotorstatusBandF(EMotorstatus.AN);
//                        System.out.println("Führe Werkstück ab");
                    }
                    /*if (fraesmaschine.initiatorF() == ESensorstatus.KEIN_SIGNAL &&
                            fraesmaschine.istAusschleussbahnBelegtF() == ESensorstatus.SIGNAL) {
                        fraesmaschine.setMotorstatusBandF(EMotorstatus.AUS);
                        zustand = 0;
                    }
                    break;*/
                    if (fraesmaschine.initiatorF() == ESensorstatus.KEIN_SIGNAL &&
                            fraesmaschine.istAusschleussbahnBelegtF() == ESensorstatus.SIGNAL) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        fraesmaschine.setMotorstatusBandF(EMotorstatus.AUS);
                        zustand = 0;
//                        System.out.println("Werkstück abgeführt, neuer Durchlauf");
                    }
                    break;


            }

        }
    }

    public void terminate() {
        terminate = true;
    }
}
