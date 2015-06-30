
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
    int serviceRate;
    public QSystem(Queue customers, Queue events, int capacity, int independentInRate, int serviceRate) {
        this(customers,events,capacity,independentInRate);
        this.serviceRate = serviceRate;
    }
    public QSystem(Queue customers, Queue events, int capacity, int independentInRate) {
        this.customers = customers;
        this.events = events;
        this.capacity = capacity;
        this.independentInRate = independentInRate;
    }
    public void newArrival(Event event) {
        System.out.println("");
        if(this.customers.size() < this.capacity) {
            double t = event.time+RandVar.exponential(serviceRate);
            events.add(new Event(t, Event.DEPARTURE));
            System.out.println("customer came at: " + event.time + " and leaves at: " + t);
        }else
            System.out.println("customer came at: " + event.time);
        customers.add(new Customer(event.time));
        events.add(new Event(event.time+RandVar.exponential(independentInRate), Event.ARRIVAL));
    }
    public void newArrival(Event event, Customer customer) {
        if(this.customers.size() < this.capacity) {
            events.add(new Event(event.time+customer.serviceTime, Event.DEPARTURE));
        }
        customers.add(customer);
        events.add(new Event(event.time+RandVar.exponential(independentInRate), Event.ARRIVAL));
    }
    public void finishService(Event event) {
        Customer customer = (Customer)customers.poll();
        System.out.println("customer came at: " + customer.arrivalTime + " and leaved at: " + event.time);
        //System.out.println("time in system: " + (event.time-customer.arrivalTime));
        //first capacity-1 customer has their departure event set already
        //you should wait until begining of service to schedule departure event
        if(customers.size() >= capacity ) {
            events.add(new Event(event.time+RandVar.exponential(serviceRate), Event.DEPARTURE));
        }
    }
}
