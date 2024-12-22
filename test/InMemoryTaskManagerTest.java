import org.junit.jupiter.api.BeforeEach;
import work.managers.Managers;
import work.managers.task.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    @BeforeEach
    public void setUp() {
        manager = Managers.getDefault();
    }
}