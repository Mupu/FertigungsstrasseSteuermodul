package me.mupu.fertigungsstrasseSteuermodul.steuereinheiten;

import me.mupu.enums.motorbewegungen.EMotorbewegungXAchse;
import me.mupu.enums.motorbewegungen.EMotorbewegungYAchse;
import me.mupu.enums.motorbewegungen.EMotorbewegungZAchse;
import me.mupu.enums.motorbewegungen.EMotorstatus;
import me.mupu.enums.sensoren.ESensorXAchse;
import me.mupu.enums.sensoren.ESensorYAchse;
import me.mupu.enums.sensoren.ESensorZAchse;
import me.mupu.enums.sensoren.ESensorstatus;
import me.mupu.interfaces.maschinen.IKran;

public class SteuereinheitKran extends Thread {
    private IKran kran;
    private boolean terminate = false;

    private ImpulsCounterThread impulsCounterThread = null;
    private final int SLEEP_TIME_IN_MILLIS = 1;

    private int impulseTestResultX = 1806;
    private int impulseTestResultY = 321;
    private int impulseTestResultZ = 445;
    private int zustand = 0;

    private final int maxWerkstueckAnzahl = 1;
    private int aufgeladeneWerkstuecke = 0;


    private int grundzustandRecovery = 0;

    private int impulseX = 0;
    private int impulseY = 0;
    private int impulseZ = 0;


    private class ImpulsCounterThread extends Thread {
        private boolean terminate = false;

        private ImpulsCounterThread(Runnable r) {
            super(r);
        }

        public void terminate() {
            terminate = true;
            impulsCounterThread = null;
        }

    }


    public SteuereinheitKran(IKran kran) {
        this.kran = kran;
    }

    @Override
    public void run() {
        terminate = false;

        while (!terminate) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (zustand == -1) { // case -1 entscheidet was der kran zu tun hat
                if (kran.istAusschleussbahnBelegtK() == ESensorstatus.SIGNAL) {
                    zustand = 20; // werksuteck abladen
                } else if (kran.istEinlegestationBelegtK() == ESensorstatus.KEIN_SIGNAL
                        && aufgeladeneWerkstuecke < maxWerkstueckAnzahl) {
                    zustand = 10; // werkstueck aufladen
                } else if (aufgeladeneWerkstuecke >= maxWerkstueckAnzahl) { // richtung ablage fahren, da das das naechste ist was gemacht werden muss
                    grundzustandRecovery = -1;
                    zustand = 200; // grundzustand hinten rechts: nah an der ablage
                } else {
                    grundzustandRecovery = -1;
                    zustand = 100; // grundzustand vorne links: wenn nichts zu tun ist
                }
            }

            zuGrundzustandVorneLinksFahren();

            zuGrundzustandHintenRechtsFahren();

            impulseTests();

            werkstueckAufladen();

            werkstueckAbladen();

        }

    }

    // case 100 - 102
    private void zuGrundzustandVorneLinksFahren() {
        switch (zustand) {
            case 100: // hoch fahren
                if (kran.getPositionZAchseK() == ESensorZAchse.OBEN) {
                    kran.setMotorstatusZAchseK(EMotorbewegungZAchse.AUS);
                    zustand++;
                } else
                    kran.setMotorstatusZAchseK(EMotorbewegungZAchse.AUF);

                break;


            // #####################################################################################################
            case 101: // vorne fahren
                if (kran.getPositionYAchseK() == ESensorYAchse.VORNE) {
                    kran.setMotorstatusYAchseK(EMotorbewegungYAchse.AUS);
                    zustand++;
                } else
                    kran.setMotorstatusYAchseK(EMotorbewegungYAchse.VOR);

                break;

            // #####################################################################################################
            case 102: // links fahren
                if (kran.getPositionXAchseK() == ESensorXAchse.LINKS) {
                    kran.setMotorstatusXAchseK(EMotorbewegungXAchse.AUS);
                    zustand = grundzustandRecovery; // da weiter machen wo es soll
                } else
                    kran.setMotorstatusXAchseK(EMotorbewegungXAchse.LINKS);

                break;
        }
    }

    // case 200 - 203
    private void zuGrundzustandHintenRechtsFahren() { // todo vl verbessern
        switch (zustand) {
            case 200: // hoch fahren
                if (kran.getPositionZAchseK() == ESensorZAchse.OBEN) {
                    kran.setMotorstatusZAchseK(EMotorbewegungZAchse.AUS);
                    zustand++;
                } else
                    kran.setMotorstatusZAchseK(EMotorbewegungZAchse.AUF);

                break;

            // #####################################################################################################
            case 201: // vorne fahren wenn nicht rechts ist
                if (kran.getPositionXAchseK() != ESensorXAchse.RECHTS) { // wenn nicht rechts nach vorn und rechts fahren
                    if (kran.getPositionYAchseK() == ESensorYAchse.VORNE) {
                        kran.setMotorstatusYAchseK(EMotorbewegungYAchse.AUS);
                        zustand++;
                    } else
                        kran.setMotorstatusYAchseK(EMotorbewegungYAchse.VOR);
                } else
                    zustand += 2; // wenn rechts dann sicher nach hinten fahren

                break;

            // #####################################################################################################
            case 202: // rechts fahren
                if (kran.getPositionXAchseK() == ESensorXAchse.RECHTS) {
                    kran.setMotorstatusXAchseK(EMotorbewegungXAchse.AUS);
                    zustand++;
                } else
                    kran.setMotorstatusXAchseK(EMotorbewegungXAchse.RECHTS);

                break;

            // #####################################################################################################
            case 203: // hinten fahren
                if (kran.getPositionYAchseK() == ESensorYAchse.HINTEN) {
                    kran.setMotorstatusYAchseK(EMotorbewegungYAchse.AUS);
                    zustand = grundzustandRecovery; // da weiter machen wo es soll
                } else
                    kran.setMotorstatusYAchseK(EMotorbewegungYAchse.ZURUECK);

                break;
        }
    }

    // case 0 - 6
    private void impulseTests() {
        switch (zustand) {
            case 0:
                resetImpulsCounter();
                impulseTestResultX = 0;
                impulseTestResultY = 0;
                impulseTestResultZ = 0;

                setRecoveryZustand();
                zustand = 100; // grundzustand vorne links

                break;

            // #####################################################################################################

            case 1: // impulseTestResultX nach rechts
                if (kran.getPositionXAchseK() == ESensorXAchse.RECHTS) {
                    kran.setMotorstatusXAchseK(EMotorbewegungXAchse.AUS);
                    System.out.println("X: " + impulseTestResultX);
                    zustand++;

                } else {
                    if (impulsCounterThread == null) {
                        setupImpulsTestCounterX();
                        impulsCounterThread.start();
                        kran.setMotorstatusXAchseK(EMotorbewegungXAchse.RECHTS);
                    }
                }

                break;

            // #####################################################################################################

            case 2: // impulseTestResultX nach links
                if (kran.getPositionXAchseK() == ESensorXAchse.LINKS) {
                    kran.setMotorstatusXAchseK(EMotorbewegungXAchse.AUS);

                    impulsCounterThread.terminate();

                    impulseTestResultX = impulseTestResultX / 2; // durchschnitt
                    System.out.println("X: " + impulseTestResultX);
                    zustand++;

                } else {
                    kran.setMotorstatusXAchseK(EMotorbewegungXAchse.LINKS);

                }

                break;

            // #####################################################################################################

            case 3: // impulseTestResultZ nach ab
                if (kran.getPositionZAchseK() == ESensorZAchse.UNTEN) {
                    kran.setMotorstatusZAchseK(EMotorbewegungZAchse.AUS);

                    System.out.println("Z: " + impulseTestResultZ);

                    zustand++;

                } else {
                    if (impulsCounterThread == null) {
                        setupImpulsTestCounterZ();
                        impulsCounterThread.start();
                        kran.setMotorstatusZAchseK(EMotorbewegungZAchse.AB);
                    }
                }

                break;

            // #####################################################################################################

            case 4: // impulseTestResultZ nach auf
                if (kran.getPositionZAchseK() == ESensorZAchse.OBEN) {
                    kran.setMotorstatusZAchseK(EMotorbewegungZAchse.AUS);

                    impulsCounterThread.terminate();
                    impulseTestResultZ = impulseTestResultZ / 2; // durchschnitt
                    System.out.println("Z: " + impulseTestResultZ);
                    zustand++;

                } else {
                    kran.setMotorstatusZAchseK(EMotorbewegungZAchse.AUF);
                }

                break;

            // #####################################################################################################

            case 5: // impulseTestResultY nach rueck
                if (kran.getPositionYAchseK() == ESensorYAchse.HINTEN) {
                    kran.setMotorstatusYAchseK(EMotorbewegungYAchse.AUS);

                    System.out.println("Y: " + impulseTestResultY);
                    zustand++;

                } else {
                    if (impulsCounterThread == null) {
                        setupImpulsTestCounterY();
                        impulsCounterThread.start();
                        kran.setMotorstatusYAchseK(EMotorbewegungYAchse.ZURUECK);
                    }
                }

                break;

            // #####################################################################################################

            case 6: // impulseTestResultY nach vor
                if (kran.getPositionYAchseK() == ESensorYAchse.VORNE) {
                    kran.setMotorstatusYAchseK(EMotorbewegungYAchse.AUS);

                    impulsCounterThread.terminate();
                    impulseTestResultY = impulseTestResultY / 2; // durchschnitt
                    System.out.println("Y: " + impulseTestResultY);
                    zustand = -1; // entscheidungs case

                } else {
                    kran.setMotorstatusYAchseK(EMotorbewegungYAchse.VOR);
                }

                break;
        }
    }

    // case 10 - 17
    private void werkstueckAufladen() {
        switch (zustand) {
            case 10:
                setRecoveryZustand();
                zustand = 100; // grundzustand vorne links

                break;
            /******************
             * Werkstueck holen
             ******************/
            case 11: // x-achse
                if (impulsCounterThread == null) {
                    resetImpulsCounter();
                    setupImpulsCounterX();
                    impulsCounterThread.start();
                }

                if (impulseX >= impulseTestResultX * 0.12) { // todo anpassen
                    kran.setMotorstatusXAchseK(EMotorbewegungXAchse.AUS);

                    impulsCounterThread.terminate();
                    zustand++;
                } else {
                    kran.setMotorstatusXAchseK(EMotorbewegungXAchse.RECHTS);
                }

                break;

            case 12: // y-achse
                if (impulsCounterThread == null) {
                    resetImpulsCounter();
                    setupImpulsCounterY();
                    impulsCounterThread.start();
                }

                if (impulseY >= impulseTestResultY * 0.14) { // todo anpassen
                    kran.setMotorstatusYAchseK(EMotorbewegungYAchse.AUS);

                    impulsCounterThread.terminate();
                    zustand++;
                } else {
                    kran.setMotorstatusYAchseK(EMotorbewegungYAchse.ZURUECK);
                }

                break;

            case 13: // z-achse
                if (impulsCounterThread == null) {
                    resetImpulsCounter();
                    setupImpulsCounterZ();
                    impulsCounterThread.start();
                }

                if (impulseZ >= impulseTestResultZ * 0.88) { //todo anpassen
                    kran.setMotorstatusZAchseK(EMotorbewegungZAchse.AUS);

                    kran.setMotorstatusMagnetK(EMotorstatus.AN);

                    impulsCounterThread.terminate();
                    zustand++;
                } else {
                    kran.setMotorstatusZAchseK(EMotorbewegungZAchse.AB);
                }

                break;
            /******************
             * Werkstueck abgaben
             ******************/
            case 14:
                setRecoveryZustand();
                zustand = 100; // grundzustand vorne links

                break;

            case 15: // x-achse
                if (impulsCounterThread == null) {
                    resetImpulsCounter();
                    setupImpulsCounterX();
                    impulsCounterThread.start();
                }

                if (impulseX >= impulseTestResultX * 0.17) { // todo anpassen
                    kran.setMotorstatusXAchseK(EMotorbewegungXAchse.AUS);

                    impulsCounterThread.terminate();
                    zustand++;
                } else {
                    kran.setMotorstatusXAchseK(EMotorbewegungXAchse.RECHTS);
                }

                break;

            case 16: // y-achse
                if (impulsCounterThread == null) {
                    resetImpulsCounter();
                    setupImpulsCounterY();
                    impulsCounterThread.start();
                }

                if (impulseY >= impulseTestResultY * 0.63) { // todo anpassen
                    kran.setMotorstatusYAchseK(EMotorbewegungYAchse.AUS);

                    impulsCounterThread.terminate();
                    zustand++;
                } else {
                    kran.setMotorstatusYAchseK(EMotorbewegungYAchse.ZURUECK);
                }

                break;

            case 17: // z-achse
                if (kran.istEinlegestationBelegtK() == ESensorstatus.KEIN_SIGNAL
                        && kran.istSchieberInGrundpositionK() == ESensorstatus.SIGNAL) {

                    if (impulsCounterThread == null) {
                        resetImpulsCounter();
                        setupImpulsCounterZ();
                        impulsCounterThread.start();
                    }

                    if (impulseZ >= impulseTestResultZ * 0.66) { // todo anpassen
                        kran.setMotorstatusZAchseK(EMotorbewegungZAchse.AUS);

                        kran.setMotorstatusMagnetK(EMotorstatus.AUS);

                        impulsCounterThread.terminate();

                        aufgeladeneWerkstuecke++;
                        zustand = -1; // entscheidungs case
                    } else {
                        kran.setMotorstatusZAchseK(EMotorbewegungZAchse.AB);
                    }

                }

                break;
        }
    }

    // case 20 - 27
    private void werkstueckAbladen() {
        switch (zustand) {
            case 20:
                setRecoveryZustand();
                zustand = 200; // grundzustand hinten rechts

                break;
            /******************
             * Werkstueck holen
             ******************/
            case 21: // y-achse
                if (impulsCounterThread == null) {
                    resetImpulsCounter();
                    setupImpulsCounterY();
                    impulsCounterThread.start();
                }

                if (impulseY >= impulseTestResultY * 0.31) { // todo anpassen
                    kran.setMotorstatusYAchseK(EMotorbewegungYAchse.AUS);

                    impulsCounterThread.terminate();
                    zustand++;
                } else {
                    kran.setMotorstatusYAchseK(EMotorbewegungYAchse.VOR);
                }

                break;


            case 22: // x-achse
                if (impulsCounterThread == null) {
                    resetImpulsCounter();
                    setupImpulsCounterX();
                    impulsCounterThread.start();
                }

                if (impulseX >= impulseTestResultX * 0.15) { // todo anpassen
                    kran.setMotorstatusXAchseK(EMotorbewegungXAchse.AUS);

                    impulsCounterThread.terminate();
                    zustand++;
                } else {
                    kran.setMotorstatusXAchseK(EMotorbewegungXAchse.LINKS);
                }

                break;

            case 23: // z-achse
                if (kran.istAusschleussbahnBelegtK() == ESensorstatus.SIGNAL) {

                    if (impulsCounterThread == null) {
                        resetImpulsCounter();
                        setupImpulsCounterZ();
                        impulsCounterThread.start();
                    }

                    if (impulseZ >= impulseTestResultZ * 0.62) { // todo anpassen
                        kran.setMotorstatusZAchseK(EMotorbewegungZAchse.AUS);

                        kran.setMotorstatusMagnetK(EMotorstatus.AN);

                        impulsCounterThread.terminate();
                        zustand++;
                    } else {
                        kran.setMotorstatusZAchseK(EMotorbewegungZAchse.AB);
                    }

                }

                break;
            /******************
             * Werkstueck abgaben
             ******************/
            case 24: // z-achse
                if (kran.getPositionZAchseK() == ESensorZAchse.OBEN) {
                    kran.setMotorstatusZAchseK(EMotorbewegungZAchse.AUS);
                    zustand++;
                } else {
                    kran.setMotorstatusZAchseK(EMotorbewegungZAchse.AUF);
                }

                break;

            case 25: // x-achse
                if (kran.getPositionXAchseK() == ESensorXAchse.RECHTS) {
                    kran.setMotorstatusXAchseK(EMotorbewegungXAchse.AUS);
                    zustand++;
                } else {
                    kran.setMotorstatusXAchseK(EMotorbewegungXAchse.RECHTS);
                }

                break;

//            case 26: // x-achse
//                if (impulsCounterThread == null) {
//                    resetImpulsCounter();
//                    setupImpulsCounterX();
//                    impulsCounterThread.start();
//                }
//
//                if (impulseX >= impulseTestResultX * 0.10) { // todo anpassen
//                    kran.setMotorstatusXAchseK(EMotorbewegungXAchse.AUS);
//
//                    impulsCounterThread.terminate();
//                    zustand++;
//                } else {
//                    kran.setMotorstatusXAchseK(EMotorbewegungXAchse.LINKS);
//                }
//
//                break;

            case 26: // z-achse
                if (impulsCounterThread == null) {
                    resetImpulsCounter();
                    setupImpulsCounterZ();
                    impulsCounterThread.start();
                }

                if (impulseZ >= impulseTestResultZ * 0.80) { // todo anpassen
                    kran.setMotorstatusZAchseK(EMotorbewegungZAchse.AUS);

                    kran.setMotorstatusMagnetK(EMotorstatus.AUS);

                    impulsCounterThread.terminate();

                    aufgeladeneWerkstuecke--;
                    zustand = -1; // entscheidungs case
                } else {
                    kran.setMotorstatusZAchseK(EMotorbewegungZAchse.AB);
                }

                break;
        }
    }

    private void setRecoveryZustand() {
        grundzustandRecovery = zustand + 1;
    }

    private void resetImpulsCounter() {
        impulseX = 0;
        impulseY = 0;
        impulseZ = 0;
    }

    private void setupImpulsTestCounterX() {
        impulsCounterThread = new ImpulsCounterThread(() -> {
            try {
                ESensorstatus eSS = kran.initiatorXAchseK();
                while (!impulsCounterThread.terminate) {
                    while (kran.initiatorXAchseK() == eSS) {
                        try {
                            Thread.sleep(SLEEP_TIME_IN_MILLIS);
                        } catch (Exception e) {
                        }
                    }
                    impulseTestResultX++;
                    while (kran.initiatorXAchseK() != eSS) {
                        try {
                            Thread.sleep(SLEEP_TIME_IN_MILLIS);
                        } catch (Exception e) {
                        }
                    }
                    impulseTestResultX++;
                }
            } catch (Exception e) {
                if (impulsCounterThread != null)
                    impulsCounterThread.stop();
            }
        });
    }

    private void setupImpulsTestCounterY() {
        impulsCounterThread = new ImpulsCounterThread(() -> {
            try {
                ESensorstatus eSS = kran.initiatorYAchseK();
                while (!impulsCounterThread.terminate) {
                    while (kran.initiatorYAchseK() == eSS) {
                        try {
                            Thread.sleep(SLEEP_TIME_IN_MILLIS);
                        } catch (Exception e) {
                        }
                    }
                    impulseTestResultY++;
                    while (kran.initiatorYAchseK() != eSS) {
                        try {
                            Thread.sleep(SLEEP_TIME_IN_MILLIS);
                        } catch (Exception e) {
                        }
                    }
                    impulseTestResultY++;
                }
            } catch (Exception e) {
                if (impulsCounterThread != null)
                    impulsCounterThread.stop();
            }
        });
    }

    private void setupImpulsTestCounterZ() {
        impulsCounterThread = new ImpulsCounterThread(() -> {
            try {
                ESensorstatus eSS = kran.initiatorZAchseK();
                while (!impulsCounterThread.terminate) {
                    while (kran.initiatorZAchseK() == eSS) {
                        try {
                            Thread.sleep(SLEEP_TIME_IN_MILLIS);
                        } catch (Exception e) {
                        }
                    }
                    impulseTestResultZ++;
                    while (kran.initiatorZAchseK() != eSS) {
                        try {
                            Thread.sleep(SLEEP_TIME_IN_MILLIS);
                        } catch (Exception e) {
                        }
                    }
                    impulseTestResultZ++;
                }
            } catch (Exception e) {
                if (impulsCounterThread != null)
                    impulsCounterThread.stop();
            }
        });
    }

    private void setupImpulsCounterX() {
        impulsCounterThread = new ImpulsCounterThread(() -> {
            try {
                ESensorstatus eSS = kran.initiatorXAchseK();
                while (!impulsCounterThread.terminate) {
                    while (kran.initiatorXAchseK() == eSS) {
                        try {
                            Thread.sleep(SLEEP_TIME_IN_MILLIS);
                        } catch (Exception e) {
                        }
                    }
                    impulseX++;
                    while (kran.initiatorXAchseK() != eSS) {
                        try {
                            Thread.sleep(SLEEP_TIME_IN_MILLIS);
                        } catch (Exception e) {
                        }
                    }
                    impulseX++;
                }
            } catch (Exception e) {
                if (impulsCounterThread != null)
                    impulsCounterThread.stop();
            }
        });
    }

    private void setupImpulsCounterY() {
        impulsCounterThread = new ImpulsCounterThread(() -> {
            try {
                ESensorstatus eSS = kran.initiatorYAchseK();
                while (!impulsCounterThread.terminate) {
                    while (kran.initiatorYAchseK() == eSS) {
                        try {
                            Thread.sleep(SLEEP_TIME_IN_MILLIS);
                        } catch (Exception e) {
                        }
                    }
                    impulseY++;
                    while (kran.initiatorYAchseK() != eSS) {
                        try {
                            Thread.sleep(SLEEP_TIME_IN_MILLIS);
                        } catch (Exception e) {
                        }
                    }
                    impulseY++;
                }
            } catch (Exception e) {
                if (impulsCounterThread != null)
                    impulsCounterThread.stop();
            }
        });
    }

    private void setupImpulsCounterZ() {
        impulsCounterThread = new ImpulsCounterThread(() -> {
            try {
                ESensorstatus eSS = kran.initiatorZAchseK();
                while (!impulsCounterThread.terminate) {
                    while (kran.initiatorZAchseK() == eSS) {
                        try {
                            Thread.sleep(SLEEP_TIME_IN_MILLIS);
                        } catch (Exception e) {
                        }
                    }
                    impulseZ++;
                    while (kran.initiatorZAchseK() != eSS) {
                        try {
                            Thread.sleep(SLEEP_TIME_IN_MILLIS);
                        } catch (Exception e) {
                        }
                    }
                    impulseZ++;
                }
            } catch (Exception e) {
                if (impulsCounterThread != null)
                    impulsCounterThread.stop();
            }
        });
    }

    public void terminate() {
        terminate = true;
    }

    public void enableKali(boolean isEnabled) {
        if (isEnabled)
            zustand = 0;
        else
            zustand = -1;
    }
}
