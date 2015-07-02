
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mahdi
 */

class EventComparator implements Comparator<Event> {
    @Override
    public int compare(Event o1, Event o2) {
        if(o1.time == o2.time)
            return 0;
        else
            return (o1.time < o2.time)? -1 : 1;
    }
}

public class Experiment {
    PriorityQueue<Event> events;
    double expTime;
    
    public Experiment(List<QSystem> systems) {
        events = new PriorityQueue<>(new EventComparator());
        for(QSystem sys : systems) {
            if(sys.independentInRate > 0) {
                events.add(new Event(0, Event.ARRIVAL, sys));
            }
            sys.events = events;
        }
        expTime = 0.0;
    }
    
    public void run() throws Exception {
        for(int i=0; i<1000000; ++i) {
            Event event = events.poll();
            if(event.time < expTime)
                throw new Exception();
            expTime = event.time;
            if(event.type == Event.ARRIVAL) {
                event.sys.newArrival(event);
            }else{
                event.sys.finishService(event);
            }
        }
    }

    double getTime() {
        return expTime;
    }
}
