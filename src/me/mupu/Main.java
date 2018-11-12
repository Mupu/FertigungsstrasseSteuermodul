package me.mupu;

import me.mupu.enums.motorbewegungen.EMotorstatus;
import me.mupu.fertigungsstrasseSteuermodul.Steuermodul;
import me.mupu.interfaces.maschinen.IMSchieber;
import quancom.UsbOptoRel32;

/**
 *
 */
public class Main {
    public static void main(String[] args) {
        Steuermodul steuermodul = new Steuermodul();
        steuermodul.start();
    }

}
