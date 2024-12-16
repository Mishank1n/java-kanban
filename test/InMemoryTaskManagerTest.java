import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import work.managers.Managers;
import work.managers.files.ManagerSaveException;
import work.managers.task.InMemoryTaskManager;
import work.status.TaskStatus;
import work.types.Epic;
import work.types.SubTask;
import work.types.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    @BeforeEach
    public void setUp() {
        manager = Managers.getDefault();
    }
}