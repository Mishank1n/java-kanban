package work.types;

import work.status.TaskStatus;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTaskIds = new ArrayList<>();

    public Epic(String title, String descriptionOfTask) {
        super(title, descriptionOfTask, TaskStatus.NEW, Type.EPIC);
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

}
