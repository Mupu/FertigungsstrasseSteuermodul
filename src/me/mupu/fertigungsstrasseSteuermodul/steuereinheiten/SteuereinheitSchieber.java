package me.mupu.fertigungsstrasseSteuermodul.steuereinheiten;

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
