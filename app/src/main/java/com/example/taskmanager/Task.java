package com.example.taskmanager;

public class Task {
    private int taskId;
    private int eventId;
    private String name;
    private boolean done;
    private int priority;

    public Task(int taskId, int eventId, String name, boolean done, int priority) {
        this.taskId = taskId;
        this.eventId = eventId;
        this.name = name;
        this.done = done;
        this.priority = priority;
    }


    public int getTaskId() {
        return taskId;
    }

    public int getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }

    public boolean isDone() {
        return done;
    }

    public int getPriority() {
        return priority;
    }
}
