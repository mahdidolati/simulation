
import java.util.Comparator;
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

class SjfComparator implements Comparator<Customer> {
    @Override
    public int compare(Customer o1, Customer o2) {
        if(o1.serviceTime == o2.serviceTime)
            return 0;
        else
            return (o1.serviceTime < o2.serviceTime)? -1 : 1;
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
        //create network
        PriorityQueue<Customer> fifoCustomers = new PriorityQueue<>(new FifoComparator());
        PriorityQueue<Customer> sjfCustomers = new PriorityQueue<>(new SjfComparator());
        PriorityQueue<Customer> fifoCustomers2 = new PriorityQueue<>(new FifoComparator());
        QSystem proc = new QSystem(fifoCustomers2, 7, 0, 1.2, null);
        QSystem pre1 = new SjfSystem(sjfCustomers, 1, 7, 6, 100, proc);
        QSystem pre2 = new QSystem(fifoCustomers, 1, 2, 3, proc);
        List<QSystem> systems = new LinkedList<>();
        systems.add(proc);
        systems.add(pre1);
        systems.add(pre2);
        //create experiment and run it
        Experiment experiment = new Experiment(systems);
        experiment.run();
        //get outputs
        double expTime = experiment.getTime();
        System.out.println("lambda proc: "+(proc.totalCustomers/expTime)+" after: "+expTime);
        System.out.println("lambda pre1: "+(pre1.totalCustomers/expTime)+" after: "+expTime);
        System.out.println("lambda pre2: "+(pre2.totalCustomers/expTime)+" after: "+expTime);
        System.out.println("mu proc: "+(proc.servicedCustomers/expTime)+" after: "+expTime);
        System.out.println("mu pre1: "+(pre1.servicedCustomers/expTime)+" after: "+expTime);
        System.out.println("eff-mu pre2: "+(pre2.servicedCustomers/expTime)+" after: "+expTime);
        System.out.println("pre1) avg wait: " + pre1.avgWaitingTime 
                + ", block prob: " + (((SjfSystem)pre1).blocked/pre1.totalCustomers) + ", srvTime: " + pre1.avgWhileSerivice);
        System.out.println("pre2) avg wait: " + pre2.avgWaitingTime + ", inQ: " + pre2.avgQTime + ", srvTime: " + pre2.avgWhileSerivice);
        System.out.println("proc) avg wait: " + proc.avgWaitingTime 
                + ", avg qLen: " + proc.qLen 
                + ", avgLen2: " + proc.qLen2 
                + ", time in q: " + proc.avgQTime 
                + ", service time: " + proc.avgWhileSerivice
        );
    }
}
