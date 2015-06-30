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
    double arrivalTime;
    double serviceTime;
    public Customer(double t) {
        this.arrivalTime = t;
    }
    public Customer(double t, double s)  {
        this.arrivalTime = t;
        this.serviceTime = s;
    }
    public void setServiceTime(double s) {
        this.serviceTime = s;
    }
}
