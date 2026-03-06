package org.example.ahd.utils.Observer;

import java.util.ArrayList;
import java.util.List;

public class Observable {
    private List<Observer> observers = new ArrayList<>();
    public void addObserver(Observer observer){
        observers.add(observer);
    }
    public void removeObserver(Observer observer){
        observers.remove(observer);
    }
    public void notifyObservers(Object o){
        for (Observer observer : observers){
            observer.doUpdate(o);
        }
    }
}
