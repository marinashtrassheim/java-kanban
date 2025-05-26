package exceptions;

public class TaskOverlapException extends Exception {

    public TaskOverlapException(String message) {
        message = "Новое время задачи пересекается с существующими";
    }

}
