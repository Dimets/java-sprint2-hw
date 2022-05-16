package taskengine;

import java.io.IOException;

public class HTTPTaskManager extends FileBackedTasksManager {

    public HTTPTaskManager(String url) throws IOException, InterruptedException {
        super(Managers.getDefaultHistory(), url);
        KVTaskClient kvTaskClient = new KVTaskClient(url);
    }

    @Override
    public void save() {
        super.save();
    }
}
