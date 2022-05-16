package taskengine;

import history.HistoryManager;
import history.InMemoryHistoryManager;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static TaskManager getFileManager() {
        String file =  "DbTaskManager.csv";
        return new FileBackedTasksManager(getDefaultHistory(), file);
    }

    public static TaskManager getFileManager(String pathname) {
        return new FileBackedTasksManager(getDefaultHistory(), pathname);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
