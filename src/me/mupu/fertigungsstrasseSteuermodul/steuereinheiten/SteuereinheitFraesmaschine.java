package me.mupu.fertigungsstrasseSteuermodul.steuereinheiten;

import me.mupu.interfaces.maschinen.IMFraesmaschine;

public class SteuereinheitFraesmaschine extends Thread {
    private IMFraesmaschine fraesmaschine;
    private boolean terminate;

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
