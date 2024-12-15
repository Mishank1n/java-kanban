package work.types;

import work.status.TaskStatus;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String title, String descriptionOfTask, TaskStatus status, int epicId) {
        super(title, descriptionOfTask, status, Type.SUBTASK);
        this.epicId = epicId;

    }

    public SubTask(String title, String descriptionOfTask, TaskStatus status, String startTime, String duration, int epicId) {
        super(title, descriptionOfTask, status, Type.SUBTASK, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

}
