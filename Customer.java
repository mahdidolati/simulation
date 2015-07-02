/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mahdi
 */
public class Customer {
    double init_arrival;
    double arrivalTime;
    double serviceTime;
    double inQ;
    double id;
    double srvBeg;
    double allService;
    public Customer(int id, double t) {
        this.arrivalTime = t;
        this.id = id;
    }
    public Customer(int id, double t, double s)  {
        this(id, t);
        this.serviceTime = s;
    }
    public void setServiceTime(double s) {
        this.serviceTime = s;
    }
}
