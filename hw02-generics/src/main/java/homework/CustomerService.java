package homework;


import java.util.*;

public class CustomerService {
    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    private TreeMap<Customer, String>  map = new TreeMap<Customer, String>( Comparator.comparingLong(o -> o.getScores()) );
    private Map.Entry copyEntry(Map.Entry<Customer, String> entry) {
       if ( entry != null ) {
           return new AbstractMap.SimpleEntry( new Customer( entry.getKey().getId(), entry.getKey().getName(), entry.getKey().getScores() ), entry.getValue() );
       }
       else {
           return null;
       }
    }

    public Map.Entry<Customer, String> getSmallest() {
        return copyEntry(map.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer){
        return copyEntry(map.higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        map.put(new Customer(customer.getId(),customer.getName(),customer.getScores()),data);
    }
}
