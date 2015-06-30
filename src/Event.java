/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mahdi
 */
public class Event {
    double time;
    int type;
    static int ARRIVAL = 0;
    static int DEPARTURE = 1;
    public Event(double t, int y) {
        this.time = t;
        this.type = y;
    }
}
