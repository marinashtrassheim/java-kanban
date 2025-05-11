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
        //manager.deleteAll();

        //Тестируем выгзузку из файла
        File file = new File("/Users/marinamarina/IdeaProjects/test.txt");
        FileBackedTaskManager loadManager = FileBackedTaskManager.loadFromFile(file);

        System.out.println("\n=== Просмотр выгрузки из файла ===");
        System.out.println(loadManager.getAllTasksTypes());
    }

//    private static TaskManager getTaskManager() {
//        TaskManager taskManager = Managers.getDefault();
//
//        Task task1 = new Task("Task1", "description");
//        taskManager.createTask(task1); //id = 1
//        Task task2 = new Task("Task2", "description");
//        taskManager.createTask(task2); //id = 2
//        Task updatedTask = new Task("updatedTask", "description", Status.IN_PROGRESS);
//
//        Epic epic1 = new Epic("Epic1", "desc");
//        taskManager.createEpic(epic1); //id = 3
//        Epic epic2 = new Epic("Epic2", "desc");
//        taskManager.createEpic(epic2); // id =4
//        Epic updatedEpic = new Epic("UpdatedEpic", "Describe");
//
//        SubTask subTask1 = new SubTask("SubTask1", "desc", epic1);
//        taskManager.createSubTask(subTask1); //id =5
//        SubTask subTask2 = new SubTask("SubTask2", "desc", epic1);
//        taskManager.createSubTask(subTask2); //id =6
//        SubTask subTask3 = new SubTask("SubTask3", "desc", epic2);
//        taskManager.createSubTask(subTask3); //id =7
//        SubTask updatedSubTask = new SubTask("UpdatedSubTask", "desc", Status.DONE);
//        SubTask subTask4 = new SubTask("SubTask3", "desc", Status.NEW);
//        taskManager.createSubTask(subTask4);
//
//        taskManager.updateTask(task1, updatedTask);
//        taskManager.updateSubTask(subTask1, updatedSubTask);
//        taskManager.updateEpic(epic1, updatedEpic);
//        return taskManager;
//

    //Тестирование прошедших спринтов
//        TaskManager taskManager = getTaskManager();
//
//        // Выводим все задачи
//        System.out.println("=== Все задачи ===");
//        System.out.println(taskManager.getAllTasks());
//        System.out.println("\n=== Все подзадачи ===");
//        System.out.println(taskManager.getAllSubTasks());
//        System.out.println("\n=== Все эпики ===");
//        System.out.println(taskManager.getAllEpics());
//
//        // Просматриваем задачи для истории
//        System.out.println("\n=== Просмотр задач ===");
//        System.out.println(taskManager.getTask(1)); // Task
//        System.out.println(taskManager.getEpic(3)); // Epic
//        System.out.println(taskManager.getSubTask(5)); // SubTask
//
//        // Многократно просматриваем одну задачу (должна остаться только одна запись в истории)
//        for (int i = 0; i < 7; i++) {
//            taskManager.getTask(1);
//        }
//
//        System.out.println("История просмотра");
//        List<Task> history = taskManager.getHistory();
//        for (Task task : history) {
//            System.out.println(task);
//        }
    // }
}

