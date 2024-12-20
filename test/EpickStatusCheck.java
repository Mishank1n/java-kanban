import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import work.exceptions.ManagerSaveException;
import work.managers.Managers;
import work.managers.task.InMemoryTaskManager;
import work.status.TaskStatus;
import work.types.Epic;
import work.types.SubTask;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpickStatusCheck {
    private InMemoryTaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();
    }

    @Test
    public void CheckEpicStatusThanAllSubTasksIsNew() throws ManagerSaveException {
        Epic epic = new Epic("CheckEpicStatus", "description of CheckEpicStatus");
        manager.addEpic(epic);
        SubTask subTask1 = new SubTask("SubTask 1", "Description of Subtask 1", TaskStatus.NEW, "12.10.2024 11:00", "20", epic.getId());
        manager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("SubTask 2", "Description of Subtask 2", TaskStatus.NEW, "11.10.2024 18:22", "10", epic.getId());
        manager.addSubTask(subTask2);
        SubTask subTask3 = new SubTask("SubTask 3", "Description of Subtask 3", TaskStatus.NEW, "12.10.2024 12:10", "50", epic.getId());
        manager.addSubTask(subTask3);
        assertEquals(epic.getStatus(), TaskStatus.NEW);
    }

    @Test
    public void CheckEpicStatusThanAllSubTasksIsDone() throws ManagerSaveException {
        Epic epic = new Epic("CheckEpicStatus", "description of CheckEpicStatus");
        manager.addEpic(epic);
        SubTask subTask1 = new SubTask("SubTask 1", "Description of Subtask 1", TaskStatus.DONE, "12.10.2024 11:00", "20", epic.getId());
        manager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("SubTask 2", "Description of Subtask 2", TaskStatus.DONE, "11.10.2024 18:22", "10", epic.getId());
        manager.addSubTask(subTask2);
        SubTask subTask3 = new SubTask("SubTask 3", "Description of Subtask 3", TaskStatus.DONE, "12.10.2024 12:10", "50", epic.getId());
        manager.addSubTask(subTask3);
        assertEquals(epic.getStatus(), TaskStatus.DONE);
    }

    @Test
    public void CheckEpicStatusThanAllSubTasksIsNewOrDone() throws ManagerSaveException {
        Epic epic = new Epic("CheckEpicStatus", "description of CheckEpicStatus");
        manager.addEpic(epic);
        SubTask subTask1 = new SubTask("SubTask 1", "Description of Subtask 1", TaskStatus.DONE, "12.10.2024 11:00", "20", epic.getId());
        manager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("SubTask 2", "Description of Subtask 2", TaskStatus.NEW, "11.10.2024 18:22", "10", epic.getId());
        manager.addSubTask(subTask2);
        SubTask subTask3 = new SubTask("SubTask 3", "Description of Subtask 3", TaskStatus.NEW, "12.10.2024 12:10", "50", epic.getId());
        manager.addSubTask(subTask3);
        assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    public void CheckEpicStatusThanSubTasksIsInProcess() throws ManagerSaveException {
        Epic epic = new Epic("CheckEpicStatus", "description of CheckEpicStatus");
        manager.addEpic(epic);
        SubTask subTask1 = new SubTask("SubTask 1", "Description of Subtask 1", TaskStatus.IN_PROGRESS, "12.10.2024 11:00", "20", epic.getId());
        manager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("SubTask 2", "Description of Subtask 2", TaskStatus.NEW, "11.10.2024 18:22", "10", epic.getId());
        manager.addSubTask(subTask2);
        SubTask subTask3 = new SubTask("SubTask 3", "Description of Subtask 3", TaskStatus.DONE, "12.10.2024 12:10", "50", epic.getId());
        manager.addSubTask(subTask3);
        assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS);
    }
}
