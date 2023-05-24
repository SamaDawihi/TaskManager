package com.example.taskmanager;

public class Task {
    private int taskId;
    private int eventId;
    private String description;
    private boolean done;

    public Task(int taskId, int eventId, String description, boolean done) {
        this.taskId = taskId;
        this.eventId = eventId;
        this.description = description;
        this.done = done;
    }


    public int getTaskId() {
        return taskId;
    }

    public int getEventId() {
        return eventId;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return done;
    }

}
