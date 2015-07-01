
import java.util.Comparator;

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
