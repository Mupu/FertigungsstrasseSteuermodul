package me.mupu.fertigungsstrasseSteuermodul.steuereinheiten;

import me.mupu.enums.sensoren.ESensorstatus;
import me.mupu.interfaces.maschinen.IMBohrmaschine;

public class SteuereinheitBohrmaschine extends Thread {
    private IMBohrmaschine bohrmaschine;
    private boolean terminate = false;

    public SteuereinheitBohrmaschine(IMBohrmaschine bohrmaschine) {
        this.bohrmaschine = bohrmaschine;
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
