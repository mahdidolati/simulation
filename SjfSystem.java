
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import myMath.RandVar;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mahdi
 */
public class SjfSystem extends QSystem {
    int maxLen;
    double blocked;
    int inService;

    public SjfSystem(Queue customers, Queue events, int capacity, int independentInRate, double serviceRate, int maxLen, QSystem nextHop) {
        super(customers, events, capacity, independentInRate, serviceRate, nextHop);
        this.maxLen = maxLen;
        blocked = 0.0;
    }
    
    @Override
    public void newArrival(Event event) {
        totalCustomers++;
        if(customers.size() >= maxLen) {
            blocked++;
        }else{
            Customer customer = new Customer(event.time, RandVar.exponential(serviceRate));
            if(inService < capacity) {
                double t = event.time + customer.serviceTime;
                Event e = new Event(t, Event.DEPARTURE, this);
                e.cust = customer;
                events.add(e);
                inService++;
            }else
                customers.add(customer);
        }
        events.add(new Event(event.time+RandVar.exponential(independentInRate), Event.ARRIVAL, this));
    }
    
    @Override
    public void finishService(Event event) {
        Customer customer = event.cust;
        if(customers.contains(customer))
            customers.remove(customer);
        inService--;
        this.avgWaitingTime = this.avgWaitingTime*(this.servicedCustomers/(1+this.servicedCustomers))
                +(event.time-customer.arrivalTime)*(1.0/(1+this.servicedCustomers));
        this.servicedCustomers++;
        //System.out.println("customer came at: " + customer.arrivalTime + " and leaved at: " + event.time);
        //System.out.println("time in system: " + (event.time-customer.arrivalTime));
        //first capacity-1 customer has their departure event set already
        //you should wait until begining of service to schedule departure event
        if(customers.size() >= capacity ) {
            customer = (Customer)customers.poll();
            inService++;
            Event e = new Event(event.time+customer.serviceTime, Event.DEPARTURE, this);
            e.cust = customer;
            events.add(e);
        }
        if(nextHop != null)
            events.add(new Event(event.time, Event.ARRIVAL, nextHop));
    }
}