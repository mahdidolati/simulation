
import java.util.PriorityQueue;
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
    int id;
    
    public SjfSystem(PriorityQueue<Customer> customers, 
            int capacity, int independentInRate, double serviceRate, int maxLen, QSystem nextHop) {
        super(customers, capacity, independentInRate, serviceRate, nextHop);
        this.maxLen = maxLen;
        blocked = 0.0;
        id = 0;
    }
    
    @Override
    public void newArrival(Event event) {
        totalCustomers++;
        if(customers.size() >= maxLen) {
            blocked++;
        }else{
            Customer customer = new Customer(id, event.time, RandVar.exponential(serviceRate));
            id++;
            customer.init_arrival = event.time;
            customer.inQ = 0.0;
            if(inService < capacity) {
                double t = event.time + customer.serviceTime;
                Event e = new Event(t, Event.DEPARTURE, this);
                customer.srvBeg = event.time;
                customer.allService = customer.serviceTime;
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
                +(event.time-customer.init_arrival)*(1.0/(1+this.servicedCustomers));
        avgWhileSerivice = avgWhileSerivice*(this.servicedCustomers/(1+this.servicedCustomers))
                +(customer.allService)*(1.0/(1+this.servicedCustomers));
        customer.inQ = customer.srvBeg - customer.arrivalTime;
        this.servicedCustomers++;
        //first capacity-1 customer has their departure event set already
        //you should wait until begining of service to schedule departure event
        if(!customers.isEmpty()) {
            Customer nCustomer = (Customer)customers.poll();
            inService++;
            Event e = new Event(event.time+nCustomer.serviceTime, Event.DEPARTURE, this);
            nCustomer.srvBeg = event.time;
            nCustomer.allService = nCustomer.serviceTime;
            e.cust = nCustomer;
            events.add(e);
        }
        if(nextHop != null) {
            Event ev = new Event(event.time, Event.ARRIVAL, nextHop);
            ev.cust = customer;
            events.add(ev);
        }
    }
}
