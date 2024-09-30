public class SubTask extends Task {
    private int epicId;

    public SubTask(String title, String descriptionOfTask, TASK_STATUS status, int epicId) {
        super(title, descriptionOfTask, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

}
