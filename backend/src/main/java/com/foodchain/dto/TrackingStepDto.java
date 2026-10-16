package com.foodchain.dto;

public class TrackingStepDto {
    private String step;
    private boolean completed;
    private boolean current;
    private String time;
    private String description;

    public TrackingStepDto() {}

    public TrackingStepDto(String step, boolean completed, boolean current, String time, String description) {
        this.step = step;
        this.completed = completed;
        this.current = current;
        this.time = time;
        this.description = description;
    }

    public String getStep() { return step; }
    public void setStep(String step) { this.step = step; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public boolean isCurrent() { return current; }
    public void setCurrent(boolean current) { this.current = current; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
