package me.mupu.fertigungsstrasseSteuermodul.steuereinheiten;

import me.mupu.enums.sensoren.ESensorXAchse;
import me.mupu.interfaces.maschinen.IKran;

public class SteuereinheitKran extends Thread {
    private IKran kran;
    private boolean terminate = false;

    public SteuereinheitKran(IKran kran) {
        this.kran = kran;
    }

    @Override
    public void run() {
        terminate = false;

        int zustand = 0;
        while (!terminate) {
            // Euer Code hier
            switch (zustand) {
                case 0:
                    break;
                case 1:
                    break;
            }
        }
    }

    public void terminate() {
        terminate = true;
    }
}
