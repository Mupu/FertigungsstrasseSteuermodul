package me.mupu.fertigungsstrasseSteuermodul.steuereinheiten;

import me.mupu.enums.motorbewegungen.EMotorbewegungZAchse;
import me.mupu.enums.motorbewegungen.EMotorstatus;
import me.mupu.enums.sensoren.ESensorZAchse;
import me.mupu.enums.sensoren.ESensorstatus;
import me.mupu.interfaces.maschinen.IMBohrmaschine;

public class SteuereinheitBohrmaschine extends Thread {
    private IMBohrmaschine bohrmaschine;
    private boolean terminate = false;
    private int count = 0;

    public SteuereinheitBohrmaschine(IMBohrmaschine bohrmaschine) {
        this.bohrmaschine = bohrmaschine;
    }

    @Override
    public void run() {
        terminate = false;
        boolean werkstueckAbgegeben = false;

        int zustand = 0;
        while (!terminate) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            switch (zustand) {

                case 0:
                    if (bohrmaschine.getPositionHubB() != ESensorZAchse.OBEN) {
                        bohrmaschine.setMotorstatusHubB(EMotorbewegungZAchse.AUF);
                        //System.out.print("Bohrmaschine fährt hoch");
                    } else {
                        bohrmaschine.setMotorstatusHubB(EMotorbewegungZAchse.AUS);
                        bohrmaschine.setMotorstatusWerkzeugAntriebB(EMotorstatus.AUS);
                        //System.out.print("Bohrmaschine aus");

                        if (bohrmaschine.istUebergabestelleVorBohrmaschineBelegtB() == ESensorstatus.SIGNAL) {
                            bohrmaschine.setMotorstatusBandB(EMotorstatus.AN);
                            //System.out.print("Bohrmaschine Band an");
                            zustand++;
                            //System.out.print("Bohrmaschine "+zustand);
                        }
                    }
                    break;

                case 1:
                    if (bohrmaschine.initiatorB() == ESensorstatus.SIGNAL) {
                        bohrmaschine.setMotorstatusBandB(EMotorstatus.AUS);
                        //System.out.print("Bohrmaschine Band aus");

                        bohrmaschine.setMotorstatusWerkzeugAntriebB(EMotorstatus.AN);
                        //System.out.print("Bohrmaschine an");
                        zustand++;
                        //System.out.print("Bohrmaschine "+zustand);
                    }
                    break;

                case 2:
                    if (bohrmaschine.getPositionHubB() != ESensorZAchse.UNTEN) {
                        bohrmaschine.setMotorstatusHubB(EMotorbewegungZAchse.AB);
                        //System.out.print("Bohrmaschine fährt runter");
                    } else {
                        bohrmaschine.setMotorstatusHubB(EMotorbewegungZAchse.AUS);
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        zustand++;
                        //System.out.print("Bohrmaschine "+zustand);
                    }
                    break;

                case 3:
                    if (count < 2) {
                        if (bohrmaschine.getPositionHubB() != ESensorZAchse.OBEN) {
                            bohrmaschine.setMotorstatusHubB(EMotorbewegungZAchse.AUF);
                            //System.out.print("Bohrmaschine fährt hoch");
                        } else {
                            bohrmaschine.setMotorstatusHubB(EMotorbewegungZAchse.AUS);
                            count++;
                            zustand--;
                            //System.out.print("Bohrmaschine " + zustand);
                        }

                    } else {
                        if (bohrmaschine.getPositionHubB() != ESensorZAchse.OBEN) {
                            bohrmaschine.setMotorstatusHubB(EMotorbewegungZAchse.AUF);
                            //System.out.print("Bohrmaschine fährt hoch");
                        } else {
                            this.count = 0;
                            bohrmaschine.setMotorstatusHubB(EMotorbewegungZAchse.AUS);
                            bohrmaschine.setMotorstatusWerkzeugAntriebB(EMotorstatus.AUS);
                            //System.out.print("Bohrmaschine aus");
                            bohrmaschine.setFlagWillWerkstueckAbgebenB(true);
                            //System.out.print("Bohrmaschine Werkstück fertig");
                            zustand++;
                        }
                    }

                    break;

                case 4:
                    if (bohrmaschine.istMehrspindelmaschineBelegtB() == ESensorstatus.KEIN_SIGNAL) {
                        bohrmaschine.setMotorstatusBandB(EMotorstatus.AN);
                        werkstueckAbgegeben = true;
                        //System.out.print("Bohrmaschine Band an");
                    } else {
                        bohrmaschine.setMotorstatusBandB(EMotorstatus.AUS);
                        //System.out.print("Bohrmaschine Band aus");
                        bohrmaschine.setFlagWillWerkstueckAbgebenB(false);
                        //System.out.print("Bohrmaschine Werkstück nicht fertig");
                        if (werkstueckAbgegeben) {
                            werkstueckAbgegeben = false;
                            zustand = 0;
                        }
                    }

                    break;

            }
        }
    }

    public void terminate() {
        terminate = true;
    }


}
