package utils;

import history.HistoryManager;
import history.InMemoryHistoryManager;
import taskengine.FileBackedTasksManager;
import taskengine.HTTPTaskManager;
import taskengine.InMemoryTaskManager;
import taskengine.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new HTTPTaskManager("http://localhost:8078");
    }

    public static TaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static TaskManager getHttpTaskManager() {
        return new HTTPTaskManager("http://localhost:8078");
    }

    public static TaskManager getFileBackedTasksManager() {
        String file =  "DbTaskManager.csv";
        return new FileBackedTasksManager(getDefaultHistory(), file);
    }

    public static TaskManager getFileBackedTasksManager(String pathname) {
        return new FileBackedTasksManager(getDefaultHistory(), pathname);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
