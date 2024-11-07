package work.managers.history;

import work.types.Task;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {

    private HashMap<Integer, Node> historyMap = new HashMap<>();
    private Node head = null;

    @Override
    public List<Task> getHistory() {
        List<Task> historyList = new ArrayList<>();
        Node changeNode = head;
        while (changeNode != null) {
            historyList.add(changeNode.getTask());
            changeNode = changeNode.getPrev();
        }
        return historyList;
    }

    @Override
    public void add(Task task) {
        if (historyMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(task);
    }

    public void remove(int id) {
        if (historyMap.containsKey(id)) {
            removeNode(historyMap.get(id));
            historyMap.remove(id);
        }
    }

    public void linkLast(Task task) {
        Node node = new Node(task, null, null);
        if (head == null) {
            head = node;
        } else {
            node.setPrev(head);
            head.setNext(node);
            head = node;
        }
        historyMap.put(node.getTask().getId(), node);
    }

    public void removeNode(Node node) {
        if (node.equals(head)) {
            head = head.getNext();
        } else if (node.getNext() == null) {
            node.setPrev(null);
        } else {
            node.getPrev().setNext(node.getNext());
            node.getNext().setPrev(node.getPrev());
        }
    }
}