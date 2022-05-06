package taskengine;

import org.junit.jupiter.api.BeforeAll;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    public InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager(Managers.getDefaultHistory()));
    }
}
