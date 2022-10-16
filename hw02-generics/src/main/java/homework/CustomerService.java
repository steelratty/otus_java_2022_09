package homework;


import java.util.*;
import java.util.function.Consumer;

public class CustomerService {
    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    private TreeMap<Customer, String>  map = new TreeMap<Customer, String>( Comparator.comparingLong(o -> o.getScores()) );

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> me = map.firstEntry();
        Map.Entry me2 = null;
        if (! (me ==null)) {
            // нашли, создаем новую пару для возврата
            me2 = new AbstractMap.SimpleEntry(new Customer(me.getKey().getId(), me.getKey().getName(), me.getKey().getScores()), me.getValue());
        }
        return me2;
    }

    public Map.Entry<Customer, String> getNext(Customer customer){
      //  Map.Entry<Customer, String> me = Map.Entry.copyOf(map.ceilingEntry(customer));
       // TreeMap <Customer, String>  map2 = (TreeMap<Customer, String>) map.clone();
        Map.Entry<Customer, String> me = map.higherEntry(customer);
        Map.Entry me2 = null;
        if (! (me ==null)) {
            // нашли, создаем новую пару для возврата
            me2 = new AbstractMap.SimpleEntry(new Customer(me.getKey().getId(), me.getKey().getName(), me.getKey().getScores()), me.getValue());
        }
        return me2;
    }

    public void add(Customer customer, String data) {
        map.put(new Customer(customer.getId(),customer.getName(),customer.getScores()),data);
    }
}
