package test;

import org.junit.jupiter.api.BeforeEach;
import taskengine.InMemoryTaskManager;
import utils.Managers;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void setUp() {
        taskManager = (InMemoryTaskManager) Managers.getInMemoryTaskManager();
    }
}
