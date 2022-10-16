package homework;

public class Customer {
    private final long id;
    private String name;
    private long scores;

    //todo: 1. в этом классе надо исправить ошибки

    public Customer(long id, String name, long scores) {
        this.id = id;
        this.name = name;
        this.scores = scores;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getScores() {
        return scores;
    }

    public void setScores(long scores) {
        this.scores = scores;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + String.valueOf(id) +
                ", name='" + name + '\'' +
                ", scores=" + String.valueOf(scores) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;
        // customerAsKeyTest хочет сравнение только по id
        return    (this.id == customer.id);

    }

    @Override
    public int hashCode() {
        // customerAsKeyTest хочет сравнение только по id и так же получение хэша
        int result = (int) (id ^ (id >>> 32));
        return result;
    }
}
