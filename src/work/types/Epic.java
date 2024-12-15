package work.types;

import work.status.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTaskIds = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String title, String descriptionOfTask) {
        super(title, descriptionOfTask, TaskStatus.NEW, Type.EPIC);
        Duration duration = Duration.ofHours(0);
        this.setDuration(duration);
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void setSubTaskIds(ArrayList<Integer> subTaskIds) {
        this.subTaskIds = subTaskIds;
    }

    public void addSubTaskId(Integer subTaskId) {
        subTaskIds.add(subTaskId);
    }

    public void setEndTime() {
        if (this.getDuration() != null && this.getStartTime() != null) {
            endTime = this.getStartTime().plus(this.getDuration());
        }
    }
}
