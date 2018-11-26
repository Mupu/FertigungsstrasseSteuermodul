package me.mupu.fertigungsstrasseSteuermodul.steuereinheiten;

import me.mupu.FertigungsstrasseHLD;
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

    @Override
    public void run() {
        terminate = false;
        long time = 0;
        int times = 0;
        final long WAITING_TIME_SECONDS = 5;

        int zustand = 0;
        while (!terminate) {
            switch (zustand) {
                case 0:
                    if (mehrspindelmaschine.getPositionHubM() != ESensorZAchse.OBEN) mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AUF);
                    if (mehrspindelmaschine.istRevolverAufPositionM() == ESensorstatus.KEIN_SIGNAL) mehrspindelmaschine.setMotorstatusRevolverdrehungM(EMotorstatus.AN);
                    mehrspindelmaschine.setMotorstatusWerkzeugAntriebM(EMotorstatus.AUS);
                    zustand = 1;
                    break;
                case 1:
                    if (mehrspindelmaschine.getPositionHubM() == ESensorZAchse.OBEN) {
                        mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AUS);
                        zustand = 2;
                    }
                    if (mehrspindelmaschine.istRevolverAufPositionM() == ESensorstatus.SIGNAL) {
                        mehrspindelmaschine.setMotorstatusRevolverdrehungM(EMotorstatus.AUS);
                        zustand = 3;
                    }
                    break;
                case 2:
                    if (mehrspindelmaschine.istRevolverAufPositionM() == ESensorstatus.SIGNAL) {
                        mehrspindelmaschine.setMotorstatusRevolverdrehungM(EMotorstatus.AUS);
                        zustand = 4;
                    }
                    break;
                case 3:
                    if (mehrspindelmaschine.getPositionHubM() == ESensorZAchse.OBEN) {
                        mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AUS);
                        zustand = 4;
                    }
                    break;
                case 4:
                    if (mehrspindelmaschine.initiatorM() == ESensorstatus.SIGNAL) {
                        mehrspindelmaschine.setFlagWillWerkstueckAbgebenM(false);
                        mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AB);
                        zustand = 6;
                    } else if(mehrspindelmaschine.sollWerkstueckAnnehmenM() == ESensorstatus.SIGNAL) {
                        mehrspindelmaschine.setMotorstatusBandM(EMotorstatus.AN);
                        zustand = 5;
                    }
                    break;
                case 5:
                    if (mehrspindelmaschine.initiatorM() == ESensorstatus.SIGNAL) {
                        mehrspindelmaschine.setFlagWillWerkstueckAbgebenM(false);
                        mehrspindelmaschine.setMotorstatusBandM(EMotorstatus.AUS);
                        mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AB);
                        zustand = 6;
                    }
                    break;
                case 6:
                    if (mehrspindelmaschine.getPositionHubM() == ESensorZAchse.UNTEN) {
                        mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AUS);
                        mehrspindelmaschine.setMotorstatusWerkzeugAntriebM(EMotorstatus.AN);
                        time = System.currentTimeMillis();
                        zustand = 7;
                    }
                    break;
                case 7:
                    if (System.currentTimeMillis() >= time + WAITING_TIME_SECONDS * 1000) {
                        mehrspindelmaschine.setMotorstatusWerkzeugAntriebM(EMotorstatus.AUS);
                        mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AUF);
                        zustand = 8;
                    }
                    break;
                case 8:
                    if (mehrspindelmaschine.getPositionHubM() == ESensorZAchse.OBEN) {
                        mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AUS);
                        if (times >= 2) {
                            if (mehrspindelmaschine.istFraesmaschineBelegtM() == ESensorstatus.KEIN_SIGNAL) {
                                mehrspindelmaschine.setFlagWillWerkstueckAbgebenM(true);
                                mehrspindelmaschine.setMotorstatusBandM(EMotorstatus.AN);
                                times = 0;
                                zustand = 4;
                            }
                        } else {
                            mehrspindelmaschine.setMotorstatusRevolverdrehungM(EMotorstatus.AN);
                            times++;
                            zustand = 9;
                        }
                    }
                    break;
                case 9:
                    if (mehrspindelmaschine.istRevolverAufPositionM() == ESensorstatus.SIGNAL) {
                        mehrspindelmaschine.setMotorstatusRevolverdrehungM(EMotorstatus.AUS);
                        mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AB);
                        zustand = 6;
                    }
                    break;
            }
        }
    }

    public void terminate() {
        terminate = true;
        mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AUS);
        mehrspindelmaschine.setMotorstatusRevolverdrehungM(EMotorstatus.AUS);
        mehrspindelmaschine.setMotorstatusBandM(EMotorstatus.AUS);
        mehrspindelmaschine.setMotorstatusWerkzeugAntriebM(EMotorstatus.AUS);
    }
}
