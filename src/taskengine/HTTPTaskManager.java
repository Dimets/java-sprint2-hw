package taskengine;

import com.google.gson.*;
import history.InMemoryHistoryManager;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import utils.KVTaskClient;
import utils.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

public class HTTPTaskManager extends FileBackedTasksManager {
    private final KVTaskClient kvTaskClient;
    private static final Gson gson = new Gson();

    public HTTPTaskManager(String url)  {
        super(Managers.getDefaultHistory(), null);
        this.kvTaskClient = new KVTaskClient(url);
    }

    public KVTaskClient getKvTaskClient() {
        return kvTaskClient;
    }

    @Override
    public void save() {
        kvTaskClient.put("tasks", gson.toJson(super.getTasks()));
        kvTaskClient.put("subtasks", gson.toJson(super.getSubTasks()));
        kvTaskClient.put("epics", gson.toJson(super.getEpics()));
        kvTaskClient.put("history", gson.toJson(InMemoryHistoryManager.toString(getHistoryManager())));
        kvTaskClient.put("priority", gson.toJson(super.getPrioritizedTasks()));
    }
    public static TaskManager load() {
        HTTPTaskManager httpTaskManager = (HTTPTaskManager) Managers.getHttpTaskManager();
        KVTaskClient kvTaskClient = httpTaskManager.getKvTaskClient();
        LocalDateTime startTime;
        Duration duration;


        if (kvTaskClient.load("tasks") != null) {
            JsonElement tasksJsonElement = JsonParser.parseString(kvTaskClient.load("tasks"));

            for (Object taskElement : gson.fromJson(tasksJsonElement, HashMap.class).values()) {
                startTime = null;
                duration = null;

                JsonObject jsonObject = JsonParser.parseString(gson.toJson(taskElement)).getAsJsonObject();

                if (jsonObject.has("startTime")) {
                    startTime = gson.fromJson(jsonObject.get("startTime"), LocalDateTime.class);
                }

                if (jsonObject.has("duration")) {
                    duration = gson.fromJson(jsonObject.get("duration"), Duration.class);
                }

                httpTaskManager.createTask(new Task(jsonObject.get("name").getAsString(),
                        jsonObject.get("description").getAsString(), jsonObject.get("id").getAsInt(),
                        TaskStatus.valueOf(jsonObject.get("status").getAsString()),startTime,duration));
            }
        }

        if (kvTaskClient.load("subtasks") != null) {
            JsonElement subTasksJsonElement = JsonParser.parseString(kvTaskClient.load("subtasks"));
            for (Object subTaskElement : gson.fromJson(subTasksJsonElement, HashMap.class).values()) {
                startTime = null;
                duration = null;

                JsonObject jsonObject = JsonParser.parseString(gson.toJson(subTaskElement)).getAsJsonObject();

                if (jsonObject.has("startTime")) {
                    startTime = gson.fromJson(jsonObject.get("startTime"), LocalDateTime.class);
                }

                if (jsonObject.has("duration")) {
                    duration = gson.fromJson(jsonObject.get("duration"), Duration.class);
                }

                httpTaskManager.createSubTask(new SubTask(jsonObject.get("name").getAsString(),
                        jsonObject.get("description").getAsString(), jsonObject.get("id").getAsInt(),
                        TaskStatus.valueOf(jsonObject.get("status").getAsString()),jsonObject.get("epicId").getAsInt(),
                        startTime, duration));
            }
        }

        if (kvTaskClient.load("epics") != null) {
            JsonElement epicsJsonElement = JsonParser.parseString(kvTaskClient.load("epics"));
            for (Object epicElement : gson.fromJson(epicsJsonElement, HashMap.class).values()) {

                JsonObject jsonObject = JsonParser.parseString(gson.toJson(epicElement)).getAsJsonObject();

                httpTaskManager.createEpic(new Epic(jsonObject.get("name").getAsString(),
                        jsonObject.get("description").getAsString(), jsonObject.get("id").getAsInt(),
                        TaskStatus.valueOf(jsonObject.get("status").getAsString())));
            }
        }

        if (kvTaskClient.load("history").length() == 0) {
            JsonElement historyJsonElement = JsonParser.parseString(kvTaskClient.load("history"));

            String[] idInHistory = historyJsonElement.getAsString().split(",");
            for (String id : idInHistory) {
                if (httpTaskManager.getTasks().containsKey(Integer.parseInt(id))) {
                    httpTaskManager.getTaskById(Integer.parseInt(id));
                } else if (httpTaskManager.getEpics().containsKey(Integer.parseInt(id))) {
                    httpTaskManager.getEpicById(Integer.parseInt(id));
                } else {
                    httpTaskManager.getSubTaskById(Integer.parseInt(id));
                }
            }
        }

        return httpTaskManager;
    }
}
