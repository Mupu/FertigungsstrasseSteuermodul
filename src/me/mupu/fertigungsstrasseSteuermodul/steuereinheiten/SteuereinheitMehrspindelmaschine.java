package me.mupu.fertigungsstrasseSteuermodul.steuereinheiten;

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
