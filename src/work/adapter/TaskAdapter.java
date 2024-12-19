package work.adapter;

import work.status.TaskStatus;
import work.types.Type;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskAdapter {
    private final Type type;
    private String title;
    private int id;
    private String descriptionOfTask;
    private TaskStatus status;
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public TaskAdapter(Type type, String title, int id, String descriptionOfTask, TaskStatus status, Duration duration, LocalDateTime startTime) {
        this.type = type;
        this.title = title;
        this.id = id;
        this.descriptionOfTask = descriptionOfTask;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = this.startTime.plus(this.duration);
    }

    public Type getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescriptionOfTask() {
        return descriptionOfTask;
    }

    public void setDescriptionOfTask(String descriptionOfTask) {
        this.descriptionOfTask = descriptionOfTask;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public TaskAdapter(Type type, String title, int id, String descriptionOfTask, TaskStatus status) {
        this.type = type;
        this.title = title;
        this.id = id;
        this.descriptionOfTask = descriptionOfTask;
        this.status = status;
    }
}
