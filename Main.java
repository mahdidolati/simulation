
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
        double mean = 0.0;
        for(int i=0; i<1000; ++i) {
            mean += RandVar.exponential(3);
        }
        System.out.println(mean/1000);
        Main.simul();
    }
    
    public static void simul() throws Exception {
        Queue<Customer> fifoCustomers = new PriorityQueue<>(new FifoComparator());
        Queue<Customer> sjfCustomers = new PriorityQueue<>(new FifoComparator());
        Queue<Event> events = new PriorityQueue<>(new EventComparator());
        Queue<Customer> fifoCustomers2 = new PriorityQueue<>(new FifoComparator());
        QSystem proc = new QSystem(fifoCustomers2, events, 8, 0, 1.2, null);
        QSystem pre1 = new SjfSystem(sjfCustomers, events, 1, 100, 6, 6, proc);
        QSystem pre2 = new QSystem(fifoCustomers, events, 1, 2, 3, proc);
        events.add(new Event(0, Event.ARRIVAL, pre1));
        events.add(new Event(0, Event.ARRIVAL, pre2));
        double tt = 0;
        for(int i=0; i<50000000; ++i) {
            Event event = events.poll();
            if(event.time < tt)
                throw new Exception();
            tt = event.time;
            if(event.type == Event.ARRIVAL) {
                event.sys.newArrival(event);
            }else{
                event.sys.finishService(event);
            }
        }
        System.out.println("pre1) avg wait: " + pre1.avgWaitingTime 
                + ", block prob: " + (((SjfSystem)pre1).blocked/pre1.totalCustomers));
        System.out.println("pre2) " + pre2.avgWaitingTime);
        System.out.println("proc) avg wait: " + proc.avgWaitingTime + ", avg qLen: " + proc.qLen + ", avgLen2: " + proc.qLen2);
    }
}
