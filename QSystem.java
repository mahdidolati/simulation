
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

//M/M/c
public class QSystem {
    Queue customers;
    Queue events;
    int capacity;
    int independentInRate;
    double serviceRate;
    QSystem nextHop;
    double totalCustomers, avgWaitingTime, servicedCustomers;
    double qLen, qLen2, PrevQChangeTime;
    
    public QSystem(Queue customers, Queue events, int capacity, int independentInRate, double serviceRate, QSystem nextHop) {
        this.customers = customers;
        this.events = events;
        this.capacity = capacity;
        this.independentInRate = independentInRate;
        this.nextHop = nextHop;
        this.serviceRate = serviceRate;
        this.avgWaitingTime = this.totalCustomers = this.servicedCustomers = qLen = qLen2 = PrevQChangeTime = 0.0;
    }
    
    public void newArrival(Event event) {
        this.totalCustomers++;
        if(this.customers.size() < this.capacity) {
            double t = event.time+RandVar.exponential(serviceRate);
            events.add(new Event(t, Event.DEPARTURE, this));
            qLen = ((totalCustomers-1)/totalCustomers)*qLen + (1/totalCustomers)*0;
            qLen2 = 0*(event.time-PrevQChangeTime)/event.time+qLen2*(PrevQChangeTime)/event.time;
            //System.out.println("customer came at: " + event.time + " and leaves at: " + t);
        }else {
            qLen = ((totalCustomers-1)/totalCustomers)*qLen + (1/totalCustomers)*(customers.size()-capacity);
            qLen2 = (customers.size()-capacity)*(event.time-PrevQChangeTime)/event.time+qLen2*(PrevQChangeTime)/event.time;
            //System.out.println("customer came at: " + event.time);
        }
        PrevQChangeTime = event.time;
        customers.add(new Customer(event.time));
        events.add(new Event(event.time+RandVar.exponential(independentInRate), Event.ARRIVAL, this));
    }
    
    public void finishService(Event event) {
        if(customers.size() <= capacity)
            qLen2 = 0*(event.time-PrevQChangeTime)/event.time+qLen2*(PrevQChangeTime)/event.time;
        else
            qLen2 = (customers.size()-capacity)*(event.time-PrevQChangeTime)/event.time+qLen2*(PrevQChangeTime)/event.time;
        PrevQChangeTime = event.time;
        Customer customer = (Customer)customers.poll();
        this.avgWaitingTime = this.avgWaitingTime*(this.servicedCustomers/(1+this.servicedCustomers))
                +(event.time-customer.arrivalTime)*(1.0/(1+this.servicedCustomers));
        this.servicedCustomers++;
        //System.out.println("customer came at: " + customer.arrivalTime + " and leaved at: " + event.time);
        //System.out.println("time in system: " + (event.time-customer.arrivalTime));
        //first capacity-1 customer has their departure event set already
        //you should wait until begining of service to schedule departure event
        if(customers.size() >= capacity ) {
            events.add(new Event(event.time+RandVar.exponential(serviceRate), Event.DEPARTURE, this));
        }
        if(nextHop != null)
            events.add(new Event(event.time, Event.ARRIVAL, nextHop));
    }
}
