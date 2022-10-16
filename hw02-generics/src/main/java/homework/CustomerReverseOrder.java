package homework;


import java.util.ArrayList;
import java.util.LinkedList;

public class CustomerReverseOrder {

    //todo: 2. надо реализовать методы этого класса
    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"
    //судя по тесту, нужен стек
    private LinkedList<Customer> list = new LinkedList<>();

    public void add(Customer customer) {
       list.push(customer);
    }

    public Customer take() {
        return list.pop();
    }
}
