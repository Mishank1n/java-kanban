package work.types;

import work.status.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private final Type type;
    private String title;
    private int id;
    private String descriptionOfTask;
    private TaskStatus status;
    private Duration duration;
    private LocalDateTime startTime;


    public Task(String title, String descriptionOfTask, TaskStatus status, Type type, String startTime, String duration) {
        this.descriptionOfTask = descriptionOfTask;
        this.status = status;
        this.title = title;
        this.type = type;
        DateTimeFormatter insertFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        this.startTime = LocalDateTime.parse(startTime, insertFormatter);
        this.duration = Duration.ofMinutes(Integer.parseInt(duration));
    }

    public Task(String title, String descriptionOfTask, TaskStatus status, String startTime, String duration) {
        this.title = title;
        this.descriptionOfTask = descriptionOfTask;
        this.status = status;
        this.type = Type.TASK;
        DateTimeFormatter insertFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        this.startTime = LocalDateTime.parse(startTime, insertFormatter);
        this.duration = Duration.ofMinutes(Integer.parseInt(duration));
    }

    public Task(String title, String descriptionOfTask, TaskStatus status, Type type) {
        this.title = title;
        this.descriptionOfTask = descriptionOfTask;
        this.status = status;
        this.type = type;
    }

    public Task(String title, String descriptionOfTask, TaskStatus status) {
        this.title = title;
        this.descriptionOfTask = descriptionOfTask;
        this.status = status;
        this.type = Type.TASK;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plus(duration);
        } else {
            return null;
        }
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        if (duration != null) {
            return duration;
        } else {
            return Duration.ofMinutes(0);
        }
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
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

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        if (this == obj) return true;
        Task newTask = (Task) obj;
        return Objects.equals(title, newTask.title) && Objects.equals(descriptionOfTask, newTask.descriptionOfTask) && Objects.equals(id, newTask.id) && Objects.equals(status, newTask.status);
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        if (title != null) {
            hashCode += title.hashCode();
        }
        hashCode *= 31;
        if (descriptionOfTask != null) {
            hashCode += descriptionOfTask.hashCode();
        }
        hashCode *= 31;
        return hashCode;
    }

    @Override
    public String toString() {
        return "Task [title=" + title + ", id=" + id + ", descriptionOfTask=" + descriptionOfTask + ", status=" + status + "]";
    }

}
