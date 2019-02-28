package me.mupu.fertigungsstrasseSteuermodul;

import me.mupu.FertigungsstrasseHLD;
import me.mupu.fertigungsstrasseSteuermodul.steuereinheiten.*;

public class Steuermodul {
    private SteuereinheitKran                   kran;
    private SteuereinheitSchieber               schieber;
    private SteuereinheitBohrmaschine           bohrmaschine;
    private SteuereinheitMehrspindelmaschine    mehrspindelmaschine;
    private SteuereinheitFraesmaschine          fraesmaschine;
    //todo bug bei dem der kran nicht richtig funktioniert wenn er kalibriert hat

    public Steuermodul() {
    }

    private boolean kranKali = false; // default aus bis bug gefixt

    public void setKranKali(boolean b) {
        kranKali = b;
    }

    public void start() {
        FertigungsstrasseHLD.open();
        kran                = new SteuereinheitKran(FertigungsstrasseHLD.getKran());
        kran.enableKali(kranKali);

        schieber            = new SteuereinheitSchieber(FertigungsstrasseHLD.getSchieber());
        bohrmaschine        = new SteuereinheitBohrmaschine(FertigungsstrasseHLD.getBohrmaschine());
        mehrspindelmaschine = new SteuereinheitMehrspindelmaschine(FertigungsstrasseHLD.getMehrspindelmaschine());
        fraesmaschine       = new SteuereinheitFraesmaschine(FertigungsstrasseHLD.getFraesmaschine());

        kran.start();
        schieber.start();
        bohrmaschine.start();
        mehrspindelmaschine.start();
        fraesmaschine.start();
    }

    public void stop() {
        kran.terminate();
        schieber.terminate();
        bohrmaschine.terminate();
        mehrspindelmaschine.terminate();
        fraesmaschine.terminate();
        FertigungsstrasseHLD.close();
    }
}
