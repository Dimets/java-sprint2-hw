package taskengine;

import com.google.gson.Gson;
import model.Task;

import java.io.IOException;

public class HTTPTaskManager extends FileBackedTasksManager {
    private final KVTaskClient kvTaskClient;
    private static final Gson gson = new Gson();

    public HTTPTaskManager(String url)  {
        super(Managers.getDefaultHistory(), null);
        this.kvTaskClient = new KVTaskClient(url);
    }

    @Override
    public void save() {
        kvTaskClient.put("tasks", gson.toJson(super.getTasks()));
        kvTaskClient.put("subtasks", gson.toJson(super.getSubTasks()));
        kvTaskClient.put("epics", gson.toJson(super.getEpics()));
        kvTaskClient.put("history", gson.toJson(super.history()));
        /*

        for (Task task : super.getTasks().values()) {
            kvTaskClient.put(String.valueOf(task.getId()), gson.toJson(task));
        }

        for (Task epic : super.getEpics().values()) {
            kvTaskClient.put(String.valueOf(epic.getId()), gson.toJson(epic));
        }

        for (Task subTask : super.getSubTasks().values()) {
            kvTaskClient.put(String.valueOf(subTask.getId()), gson.toJson(subTask));
        }

        kvTaskClient.put("history");
        */
    }
}
