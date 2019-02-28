package me.mupu.fertigungsstrasseSteuermodul.steuereinheiten;

import me.mupu.enums.motorbewegungen.EMotorbewegungZAchse;
import me.mupu.enums.motorbewegungen.EMotorstatus;
import me.mupu.enums.sensoren.ESensorZAchse;
import me.mupu.enums.sensoren.ESensorstatus;
import me.mupu.interfaces.maschinen.IMMehrspindelmaschine;

public class SteuereinheitMehrspindelmaschine extends Thread {
    private IMMehrspindelmaschine mehrspindelmaschine;
    private boolean terminate = false;

    public SteuereinheitMehrspindelmaschine(IMMehrspindelmaschine mehrspindelmaschine) {
        this.mehrspindelmaschine = mehrspindelmaschine;
    }

    private int zustand = 0;

    @Override
    public void run() {
        terminate = false;
        long time = 0;
        int times = 0;
        final long WAITING_TIME_SECONDS = 5;
        boolean ready = true, spinning = false;

        while (!terminate) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            switch (zustand) {
                case 0:
//                    System.out.println(0);
                    reset();
                    if (mehrspindelmaschine.getPositionHubM() == ESensorZAchse.OBEN && mehrspindelmaschine.istRevolverAufPositionM() == ESensorstatus.SIGNAL && mehrspindelmaschine.sollWerkstueckAnnehmenM() == ESensorstatus.SIGNAL) {
                        zustand = 1;
                    } else if (mehrspindelmaschine.istFraesmaschineBelegtM() == ESensorstatus.SIGNAL) {
                        mehrspindelmaschine.setMotorstatusBandM(EMotorstatus.AUS);
                        mehrspindelmaschine.setFlagWillWerkstueckAbgebenM(false);
                    }
                    break;
                case 1:
//                    System.out.println(1);
                    mehrspindelmaschine.setMotorstatusBandM(EMotorstatus.AN);
                    mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AUS);
                    mehrspindelmaschine.setMotorstatusRevolverdrehungM(EMotorstatus.AUS);
                    if (mehrspindelmaschine.initiatorM() == ESensorstatus.KEIN_SIGNAL) ready = true;
                    if (mehrspindelmaschine.initiatorM() == ESensorstatus.SIGNAL && ready) {
                        mehrspindelmaschine.setMotorstatusBandM(EMotorstatus.AUS);
                        mehrspindelmaschine.setFlagWillWerkstueckAbgebenM(false);
                        zustand = 2;
                    }
                    break;
                case 2:
//                    System.out.println(2);
                    mehrspindelmaschine.setMotorstatusBandM(EMotorstatus.AUS);
                    mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AB);
                    mehrspindelmaschine.setMotorstatusRevolverdrehungM(EMotorstatus.AUS);
                    if (mehrspindelmaschine.getPositionHubM() == ESensorZAchse.UNTEN) {
                        mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AUS);
                        time = System.currentTimeMillis();
                        zustand = 3;
                    }
                    break;
                case 3:
//                    System.out.println(3);
                    mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AUS);
                    mehrspindelmaschine.setMotorstatusWerkzeugAntriebM(EMotorstatus.AN);
                    if (System.currentTimeMillis() >= time + WAITING_TIME_SECONDS * 1000) {
                        mehrspindelmaschine.setMotorstatusWerkzeugAntriebM(EMotorstatus.AUS);
                        mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AUF);
                        zustand = 4;
                    }
                    break;
                case 4:
//                    System.out.println(4);
                    mehrspindelmaschine.setMotorstatusWerkzeugAntriebM(EMotorstatus.AUS);
                    if (mehrspindelmaschine.getPositionHubM() == ESensorZAchse.OBEN) {
                        mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AUS);
                        if (times >= 2) {
                            mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AUS);
                            mehrspindelmaschine.setFlagWillWerkstueckAbgebenM(true);
                            if (mehrspindelmaschine.istFraesmaschineBelegtM() == ESensorstatus.KEIN_SIGNAL) {
                                mehrspindelmaschine.setMotorstatusBandM(EMotorstatus.AN);
                                times = 0;
                                zustand = 0;
                                ready = false;
                            }
                        } else {
                            times++;
                            zustand = 5;
                        }
                    }
                    break;
                case 5:
//                    System.out.println(5);
                    mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AUS);
                    mehrspindelmaschine.setMotorstatusRevolverdrehungM(EMotorstatus.AN);
                    if (mehrspindelmaschine.istRevolverAufPositionM() == ESensorstatus.KEIN_SIGNAL) spinning = true;
                    if (mehrspindelmaschine.istRevolverAufPositionM() == ESensorstatus.SIGNAL && spinning) {
                        mehrspindelmaschine.setMotorstatusRevolverdrehungM(EMotorstatus.AUS);
                        spinning = false;
                        zustand = 2;
                    }
                    break;
            }
        }
    }

    public void reset() {
        if (mehrspindelmaschine.getPositionHubM() != ESensorZAchse.OBEN) mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AUF);
        else mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AUS);

        if (mehrspindelmaschine.istRevolverAufPositionM() == ESensorstatus.KEIN_SIGNAL) mehrspindelmaschine.setMotorstatusRevolverdrehungM(EMotorstatus.AN);
        else mehrspindelmaschine.setMotorstatusRevolverdrehungM(EMotorstatus.AUS);

        zustand = 0;
    }

    public void terminate() {
        terminate = true;
        mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AUS);
        mehrspindelmaschine.setMotorstatusRevolverdrehungM(EMotorstatus.AUS);
        mehrspindelmaschine.setMotorstatusBandM(EMotorstatus.AUS);
        mehrspindelmaschine.setMotorstatusWerkzeugAntriebM(EMotorstatus.AUS);
    }
}
