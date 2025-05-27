package exceptions;

public class TaskOverlapException extends RuntimeException {

    public TaskOverlapException() {
        super("Новое время задачи пересекается с существующими");  // Фиксированное сообщение
    }
}