import task.*;
import management.*;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        //Тестирование текущего спринта
        // Тестирование записи в файл, в строке указываем абсолютный путь, где будет файл
        FileBackedTaskManager manager = new FileBackedTaskManager("/Users/marinamarina/IdeaProjects/test.txt");

        //Задачи для теста
        Task task11 = new Task("Task11", "description");
        manager.createTask(task11);
        Task task12 = new Task("Task12", "description");
        manager.createTask(task12);
        Task taskUpdated12 = new Task("taskUpdated12", "description");
        manager.createTask(taskUpdated12);

        Epic epic11 = new Epic("Epic11", "desc");
        manager.createEpic(epic11);
        Epic epic12 = new Epic("Epic12", "desc");
        manager.createEpic(epic12);
        Epic epicUpdated12 = new Epic("epicUpdated12", "desc");
        manager.createEpic(epicUpdated12);

        SubTask subTask11 = new SubTask("SubTask11", "desc", epic11);
        manager.createSubTask(subTask11);
        SubTask subTask12 = new SubTask("subTask12", "desc", epic11);
        manager.createSubTask(subTask12);
        SubTask subTaskUpdated12 = new SubTask("subTaskUpdated12", "desc", epic12);
        manager.createSubTask(subTaskUpdated12);

        manager.updateTask(task12, taskUpdated12);
        manager.updateEpic(epic12, epicUpdated12);
        manager.updateSubTask(subTask12, subTaskUpdated12);
        manager.deleteTask(2);
        manager.deleteEpic(5);
        manager.deleteSubTask(7);
        //Проверяем файл

        //Тестируем выгзузку из файла
        File file = new File("/Users/marinamarina/IdeaProjects/test.txt");
        FileBackedTaskManager loadManager = FileBackedTaskManager.loadFromFile(file);

        System.out.println("\n=== Просмотр выгрузки из файла ===");
        System.out.println(loadManager.getAllTasksTypes());
    }
}

