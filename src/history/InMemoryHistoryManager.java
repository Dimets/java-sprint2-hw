package history;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer,Node<Task>> historyMap = new HashMap<>();
    private TaskLinkedList<Task> taskTaskLinkedList = new TaskLinkedList<>();

   private class TaskLinkedList<T> {
        private Node<T> headNode;
        private Node<T> tailNode;

        public Node<T> linkLast(T t) {
            Node<T> lastNode = tailNode;
            Node<T> newNode = new Node<>(tailNode, null, t);

            tailNode = newNode;

            if (lastNode == null) {
                headNode = newNode;
            } else {
                lastNode.setNext(newNode);
            }

            return newNode;
        }

        public List<T> getTasks() {
            List<T> historyList = new ArrayList<>();

            for (Node<T> i = headNode; i != null; i = i.getNext()) {
                historyList.add(i.getData());
            }

            return historyList;
        }

        public void removeNode(Node<T> node) {
            Node<T> prev = node.getPrev();
            Node<T> next = node.getNext();

           if (node == headNode) {
               headNode = next;
               next.setPrev(null);
               clearNode(node);
           } else if (node == tailNode) {
               prev.setNext(null);
               tailNode = prev;
               clearNode(node);
           } else {
               next.setPrev(prev);
               prev.setNext(next);
               clearNode(node);
           }
        }


        private void clearNode(Node<T> node) {
            node.setPrev(null);
            node.setNext(null);
            node.setData(null);
        }

    }


    @Override
    public void add(Task task) {
        int id = task.getId();

        if (historyMap.containsKey(id)) {
            historyMap.remove(id);
        }
        historyMap.put(id, taskTaskLinkedList.linkLast(task));


    }

    @Override
    public void remove(int id) {
        if (historyMap.containsKey(id)) {
            taskTaskLinkedList.removeNode(historyMap.get(id));
            historyMap.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return taskTaskLinkedList.getTasks();
    }

    public static String toString(HistoryManager historyManager) {
       StringBuilder historyManagerString =  new StringBuilder();
       int task;

       for (int i = 0; i < historyManager.getHistory().size(); i++) {
           if (i != (historyManager.getHistory().size() - 1)) {
               historyManagerString.append(historyManager.getHistory().get(i).getId() + ",");
           } else {
               historyManagerString.append(historyManager.getHistory().get(i).getId());
           }
       }

       return historyManagerString.toString();
    }

}
