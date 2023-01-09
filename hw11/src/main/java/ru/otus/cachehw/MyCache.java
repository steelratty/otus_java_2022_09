package ru.otus.cachehw;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы
    WeakHashMap<K,V> cashe = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
      event(key,value,"put");
      cashe.put(key,value);
    }

    @Override
    public void remove(K key) {
      event(key,cashe.get(key),"remove");
      cashe.remove(key);
    }

    @Override
    public V get(K key) {
        var l = cashe.get(key);
        event(key,l,"get");
        return l;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
         listeners.remove(listener);
    }
    @Override
    public void event(K key, V value, String strEvent){
        listeners.forEach(listener -> {
            try {
                listener.notify(key,value,strEvent);
            } catch (Exception ex) {
                    ex.printStackTrace();
            }
        }
        );
    }
}
