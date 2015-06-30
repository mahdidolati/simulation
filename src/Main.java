
import java.util.Comparator;
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

class FifoComparator implements Comparator<Customer> {
    @Override
    public int compare(Customer o1, Customer o2) {
        if(o1.arrivalTime == o2.arrivalTime)
            return 0;
        else
            return (o1.arrivalTime < o2.arrivalTime)? -1 : 1;
    }
}

class SjfComparator implements Comparator<Customer> {
    @Override
    public int compare(Customer o1, Customer o2) {
        if(o1.serviceTime == o2.serviceTime)
            return 0;
        else
            return (o1.serviceTime < o2.serviceTime)? -1 : 1;
    }
}

class EventComparator implements Comparator<Event> {
    @Override
    public int compare(Event o1, Event o2) {
        if(o1.time == o2.time)
            return 0;
        else
            return (o1.time < o2.time)? -1 : 1;
    }
}

public class Main {
    public static void main(String args[]) throws Exception {
        Queue<Customer> fifoCustomers = new PriorityQueue<>(new FifoComparator());
        Queue<Customer> sjfCustomers = new PriorityQueue<>(new FifoComparator());
        Queue<Event> events = new PriorityQueue<>(new EventComparator());
        double mean = 0.0;
        for(int i=0; i<1000; ++i) {
            mean += RandVar.exponential(3);
        }
        System.out.print(mean/1000);
        QSystem qSystem = new QSystem(fifoCustomers, events, 1, 3, 4);
        events.add(new Event(0, Event.ARRIVAL));
        double tt = 0;
        for(int i=0; i<1000; ++i) {
            for(Event e : events) {
                System.out.print("("+e.time + ", " + e.type+"),");
            }
            System.out.println("");
            Event event = events.poll();
            if(tt > event.time)
                throw new Exception("");
            tt = event.time;
            if(event.type == Event.ARRIVAL) {
                qSystem.newArrival(event);
            }else{
                qSystem.finishService(event);
            }
        }
    }
}
