public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Первая задача", "Описание первой задачи",
                taskManager.getTaskId(), TaskStatus.NEW);

        Task task2 = new Task("Вторая задача", "Описание второй задачи",
                taskManager.getTaskId(), TaskStatus.NEW);

        Epic epic1 = new Epic ("Первый эпик", "Описание первого эпика",
                taskManager.getTaskId(), TaskStatus.NEW);

        SubTask subTask1 = new SubTask("Первая подзадача", "Описание первой подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic1.getId());

        SubTask subTask2 = new SubTask("Вторая подзадача", "Описание второй подзадачи",
                taskManager.getTaskId(), TaskStatus.NEW, epic1.getId());


        taskManager.createTask(task1);
        taskManager.createEpic(epic1);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        // Изменение статуса задачи
        System.out.println("Исходный статус задачи: " + taskManager.getTaskById(1).getStatus());
        taskManager.updateTask(new Task("Первая задача", "Описание первой задачи",
                task1.getId(), TaskStatus.IN_PROGRESS));
        System.out.println("Новый статус задачи: " + taskManager.getTaskById(1).getStatus());

        // Изменение статуса подзадачи
        System.out.println("Исходный статус эпика id=" + epic1.getId() + ": " + epic1.getStatus());
        System.out.println("Подзадачи в эпике: " + epic1.getSubTasks());
        System.out.println("Исходный статус подзадачи id=" + subTask1.getId() + ": " + subTask1.getStatus());
        taskManager.updateSubTask(new SubTask("Первая подзадача", "Описание первой подзадачи",
                subTask1.getId(), TaskStatus.IN_PROGRESS, epic1.getId()));
        System.out.println("Новый статус эпика: " + epic1.getStatus());
        System.out.println("Подзадачи в эпике: " + epic1.getSubTasks());
        System.out.println("Новый статус подзадачи: " + taskManager.getSubTaskById(subTask1.getId()).getStatus());
        taskManager.updateSubTask(new SubTask("Первая подзадача", "Описание первой подзадачи",
                subTask1.getId(), TaskStatus.DONE, epic1.getId()));
        taskManager.updateSubTask(new SubTask("Вторая подзадача", "Описание второй подзадачи",
                subTask1.getId(), TaskStatus.DONE, epic1.getId()));
        System.out.println("Новый статус эпика: " + epic1.getStatus());
        System.out.println("Подзадачи в эпике: " + epic1.getSubTasks());
        System.out.println("Новый статус подзадачи: " + taskManager.getSubTaskById(subTask1.getId()).getStatus());

    }
}
