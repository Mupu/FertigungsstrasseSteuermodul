package me.mupu.fertigungsstrasseSteuermodul;

import me.mupu.FertigungsstrasseHLD;
import me.mupu.fertigungsstrasseSteuermodul.steuereinheiten.*;

public class Steuermodul {
    private SteuereinheitKran                   kran;
    private SteuereinheitSchieber               schieber;
    private SteuereinheitBohrmaschine           bohrmaschine;
    private SteuereinheitMehrspindelmaschine    mehrspindelmaschine;
    private SteuereinheitFraesmaschine          fraesmaschine;


    public Steuermodul() {
    }

    public void start() {
        FertigungsstrasseHLD.open();
        kran                = new SteuereinheitKran(FertigungsstrasseHLD.getKran());
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
