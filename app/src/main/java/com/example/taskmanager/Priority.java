package com.example.taskmanager;

public class Priority {
    int priority;
    String name;

    Priority(int p, String n){
        priority = p;
        name = n;
    }
    @Override
    public String toString(){
        return name;
    }

    public int getPriority() {
        return priority;
    }
}
