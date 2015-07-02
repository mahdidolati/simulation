
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
    PriorityQueue<Customer> customers;
    PriorityQueue<Event> events;
    int capacity;
    int independentInRate;
    double serviceRate;
    QSystem nextHop;
    double totalCustomers, avgWaitingTime, servicedCustomers;
    double qLen, qLen2, PrevQChangeTime;
    double avgQTime;
    int counter;
    double inService;
    double avgWhileSerivice;
    public QSystem(PriorityQueue<Customer> customers, 
            int capacity, int independentInRate, double serviceRate, QSystem nextHop) {
        this.customers = customers;
        this.capacity = capacity;
        this.independentInRate = independentInRate;
        this.nextHop = nextHop;
        this.serviceRate = serviceRate;
        this.avgWaitingTime = this.totalCustomers = this.servicedCustomers = qLen = qLen2 = PrevQChangeTime = 0.0;
        avgQTime = 0.0;
        counter = 0;
        avgWhileSerivice = 0.0;
    }
    
    public void newArrival(Event event) {
        this.totalCustomers++;
        Customer customer = new Customer(counter, event.time);
        customer.init_arrival = event.time;
        customer.inQ = 0.0;
        if(event.cust != null) {
            customer.init_arrival = event.cust.arrivalTime;
            customer.inQ = event.cust.inQ;
            customer.allService = event.cust.allService;
//            customer.allService = 0.0;
        }
        counter++;
        if(inService < capacity) {
            double srvTime = RandVar.exponential(serviceRate);
            double t = event.time+srvTime;
            customer.allService += srvTime;
            Event depEvent = new Event(t, Event.DEPARTURE, this);
            customer.srvBeg = event.time;
            depEvent.cust = customer;
            inService++;
            events.add(depEvent);
            qLen = ((totalCustomers-1)/totalCustomers)*qLen + (1/totalCustomers)*0;
            qLen2 = 0*(event.time-PrevQChangeTime)/event.time+qLen2*(PrevQChangeTime)/event.time;
        }else {
            customers.add(customer);
            qLen = ((totalCustomers-1)/totalCustomers)*qLen + (1/totalCustomers)*(customers.size()-1);
            qLen2 = (customers.size()-1)*(event.time-PrevQChangeTime)/event.time+qLen2*((PrevQChangeTime)/event.time);
        }
        PrevQChangeTime = event.time;
        
        if(independentInRate != 0)
            events.add(new Event(event.time+RandVar.exponential(independentInRate), Event.ARRIVAL, this));
    }
    
    public void finishService(Event event) {
        qLen2 = (customers.size())*(event.time-PrevQChangeTime)/event.time+qLen2*(PrevQChangeTime)/event.time;
        PrevQChangeTime = event.time;
        Customer customer = event.cust;
        inService--;
        this.avgWaitingTime = this.avgWaitingTime*(this.servicedCustomers/(1+this.servicedCustomers))
                +(event.time-customer.init_arrival)*(1.0/(1+this.servicedCustomers));
        customer.inQ = customer.inQ + customer.srvBeg-customer.arrivalTime;
        avgQTime = avgQTime*(this.servicedCustomers/(1+this.servicedCustomers))
                + customer.inQ*(1.0/(1+this.servicedCustomers));
        avgWhileSerivice = avgWhileSerivice*(this.servicedCustomers/(1+this.servicedCustomers))
                + customer.allService*(1.0/(1+this.servicedCustomers));
        this.servicedCustomers++;
        //first capacity-1 customer has their departure event set already
        //you should wait until begining of service to schedule departure event
        if(!customers.isEmpty()) {
            double srvTime = RandVar.exponential(serviceRate);
            Event depEvent = new Event(event.time+srvTime, Event.DEPARTURE, this);
            Customer nextInService = customers.poll();
            nextInService.srvBeg = event.time;
            nextInService.allService += srvTime;
            inService++;
            depEvent.cust = nextInService;
            events.add(depEvent);
        }
        if(nextHop != null) {
            Event ev = new Event(event.time, Event.ARRIVAL, nextHop);
            ev.cust = customer;
            events.add(ev);
        }
    }
}
