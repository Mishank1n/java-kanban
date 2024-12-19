import org.junit.jupiter.api.BeforeEach;
import work.managers.Managers;
import work.managers.task.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    @BeforeEach
    public void setUp() {
        manager = Managers.getDefault();
    }
}