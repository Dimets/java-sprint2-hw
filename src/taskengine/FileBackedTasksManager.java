package taskengine;

import history.HistoryManager;
import model.Epic;
import model.SubTask;
import model.Task;
import history.InMemoryHistoryManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private String backupFile;
    private HistoryManager historyManager;

    public FileBackedTasksManager(HistoryManager historyManager, String backupFile) {
        super(historyManager);
        this.historyManager = historyManager;
        this.backupFile = backupFile;
    }

    public File getBackupFile() {
        return new File(backupFile);
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public void save() {

        try (FileWriter writer = new FileWriter(backupFile, StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,epic,starttime,duration\n");
            for (Task task : super.getTasks().values()) {
                writer.write( task.toString() +  "\n");
            }

            for (Task epic : super.getEpics().values()) {
                writer.write( epic.toString() +  "\n");
            }

            for (Task subTask : super.getSubTasks().values()) {
                writer.write(subTask.toString() + "\n");
            }

            writer.write("\n");

            writer.write(InMemoryHistoryManager.toString(super.getHistoryManager()));

        } catch (IOException e) {
            throw new ManagerSaveException("Error to write to file");
        }
    }

    public static TaskManager loadFromFile(File file) {
        TaskManager fileBackedTasksManager = Managers.getFileManager();

        try (FileReader reader = new FileReader(file,StandardCharsets.UTF_8)) {
            String fileData = Files.readString(Path.of(file.getPath()), StandardCharsets.UTF_8);
            String[] fileLines = fileData.split("\n");

            if (fileData.length() == 0 || fileLines.length < 2) {
                System.out.println("Файл для загрузки данных пустой");
                throw new EmptyFileException();
            }

            for (int i = 1; i < fileLines.length; i++) {
                if (i < fileLines.length - 2) {
                    if (fileLines[i].split(",")[1].equals("TASK")) {
                        Task task = Task.fromString(fileLines[i]);
                        fileBackedTasksManager.createTask(task);
                    } else if (fileLines[i].split(",")[1].equals("EPIC")) {
                        Epic epic = Epic.fromString(fileLines[i]);
                        fileBackedTasksManager.createEpic(epic);
                    } else {
                        SubTask subTask = SubTask.fromString(fileLines[i]);
                        fileBackedTasksManager.createSubTask(subTask);
                    }
                }
            }

            if (fileLines[fileLines.length-2].equals("") && fileLines[fileLines.length-1].length() > 0) {
                List<Integer> idInHistory = fromString(fileLines[fileLines.length - 1]);

                for (int id : idInHistory) {
                    if (fileBackedTasksManager.getTasks().containsKey(id)) {
                        fileBackedTasksManager.getTaskById(id);
                    } else if (fileBackedTasksManager.getEpics().containsKey(id)) {
                        fileBackedTasksManager.getEpicById(id);
                    } else {
                        fileBackedTasksManager.getSubTaskById(id);
                    }
                }
            }

        } catch (IOException e) {
            throw new ManagerReadException("Error to read file");
        }
       return fileBackedTasksManager;
    }

    public static List<Integer> fromString(String value) {
        List<Integer> dataFromString = new ArrayList<>();
        String[] data = value.split(",");

        for (int i = 0; i < data.length; i++) {
            dataFromString.add(Integer.parseInt(data[i]));
        }

        return dataFromString;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteTaskById(int taskId) {
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public void deleteEpicById(int epicId) {
        super.deleteEpicById(epicId);
        save();
    }

    @Override
    public void deleteSubTaskById(int subTaskId) {
        super.deleteSubTaskById(subTaskId);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task  = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }
}
