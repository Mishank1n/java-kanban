package dataclasses;
import java.util.ArrayList;

import status.TASK_STATUS;

public class Epic extends Task{
    private ArrayList<Integer> subTaskIds = new ArrayList<>();

    public Epic(String title, String descriptionOfTask) {
        super(title, descriptionOfTask, TASK_STATUS.NEW);
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void setSubTaskIds(ArrayList<Integer> subTaskIds) {
        this.subTaskIds = subTaskIds;
    }
    
}
