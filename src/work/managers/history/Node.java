package work.managers.history;

import work.types.Task;

import java.util.Objects;

class Node {
    private Task task;
    private Node next;
    private Node prev;

    public Node(Task task, Node next, Node prev) {
        this.task = task;
        this.next = next;
        this.prev = prev;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(task, node.task) && Objects.equals(next, node.next) && Objects.equals(prev, node.prev);
    }

    @Override
    public int hashCode() {
        return Objects.hash(task, next, prev);
    }
}
