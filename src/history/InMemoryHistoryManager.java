package history;

import model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> history = new ArrayList<>();
    private int historySize = 10;

    /* Почему для реализации хранения истории я выбрал ArrayList:
    1. Судя по заданию храниться все должно в виде FIFO очереди, я бы тут применил Queue, но формально пока в теории
        про этот интерфейс еще ничего не было
    2. Исходя из того, что это FIFO и отдаваться должно именно в такой последовательности
       я добавляю новое значение в первый элемент
     */



    @Override
    public void add(Task task) {
        history.add(0, task);

        if (history.size() > historySize) {
                history.remove(10);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
