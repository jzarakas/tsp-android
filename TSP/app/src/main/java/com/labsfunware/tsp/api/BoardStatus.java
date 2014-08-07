package com.labsfunware.tsp.api;

/**
 * { "pin7": "1", "pin1": "38185", "pin8": "1", "voltage": "3.291 V", "pin2": "4785", "pin5": "1", "pin9": "1" }
 */
public class BoardStatus {
    public String pin7;
    public String pin1;
    public String pin8;
    public String voltage;
    public String pin2;
    public String pin5;
    public String pin9;

    public int getUrinal() {
        return getPin1();
    }

    public int getStall() {
        return getPin2();
    }

    public int getLights() { return getPin1(); }

    public int getPin1() {
        return Integer.valueOf(pin1);
    }

    public int getPin2() {
        return Integer.valueOf(pin2);
    }

    public int getPin5() {
        return Integer.valueOf(pin5);
    }

    public int getPin7() {
        return Integer.valueOf(pin7);
    }

    public int getPin8() {
        return Integer.valueOf(pin8);
    }

    public int getPin9() {
        return Integer.valueOf(pin9);
    }

}
