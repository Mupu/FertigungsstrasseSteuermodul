package me.mupu.fertigungsstrasseSteuermodul.steuereinheiten;

import me.mupu.enums.motorbewegungen.EMotorbewegungXAchse;
import me.mupu.enums.sensoren.ESensorXAchse;
import me.mupu.enums.sensoren.ESensorstatus;
import me.mupu.interfaces.maschinen.IMSchieber;

public class SteuereinheitSchieber extends Thread {
    private IMSchieber schieber;
    private boolean terminate = false;

    public SteuereinheitSchieber(IMSchieber schieber) {
        this.schieber = schieber;
    }

    @Override
    public void run() {
        terminate = false;

        int zustand = 0;
        while (!terminate) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Euer Code hier
            switch (zustand) {
                case 0:
                    if (schieber.getPositionSchieberS() == ESensorXAchse.LINKS) {
                        schieber.setMotorstatusSchieberS(EMotorbewegungXAchse.AUS);
                        zustand++;
                    } else
                        schieber.setMotorstatusSchieberS(EMotorbewegungXAchse.LINKS);
                    break;
                case 1:
                    if (schieber.istBohrmaschineBelegtS() == ESensorstatus.KEIN_SIGNAL &&
                            (schieber.istEinlegestationBelegtS() == ESensorstatus.SIGNAL
                                    || schieber.istUebergabestelleVorBohrmaschineBelegtS() == ESensorstatus.SIGNAL)) {
                        schieber.setFlagWillWerkstueckAbgebenS(true);
                        schieber.setMotorstatusSchieberS(EMotorbewegungXAchse.RECHTS);
                        zustand++;
                    }
                    break;
                case 2:
                    if (schieber.getPositionSchieberS() == ESensorXAchse.RECHTS) {
                        schieber.setMotorstatusSchieberS(EMotorbewegungXAchse.AUS);
                        schieber.setFlagWillWerkstueckAbgebenS(false);
                        zustand = 0;
                    }
                    break;
            }
        }
    }

    public void terminate() {
        terminate = true;
    }
}
