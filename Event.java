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
    double begOfSer;
    int type;
    QSystem sys;
    Customer cust;
    static int ARRIVAL = 0;
    static int DEPARTURE = 1;
    public Event(double t, int y, QSystem sys) {
        this.time = t;
        this.type = y;
        this.sys = sys;
        this.cust = null;
    }
}
