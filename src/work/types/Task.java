package work.types;

import work.status.TaskStatus;

import java.util.Objects;

public class Task {
    private String title;
    private int id;
    private String descriptionOfTask;
    private TaskStatus status;
    private final Type type;

    public Task(String title, String descriptionOfTask, TaskStatus status, Type type) {
        this.descriptionOfTask = descriptionOfTask;
        this.status = status;
        this.title = title;
        this.type = type;
    }

    public Task(String title, String descriptionOfTask, TaskStatus status) {
        this.title = title;
        this.descriptionOfTask = descriptionOfTask;
        this.status = status;
        this.type = Type.TASK;
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
