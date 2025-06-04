import exceptions.TaskOverlapException;
import task.*;
import management.*;

public class Main {
    public static void main(String[] args) {
        //Тестирование текущего спринта
        // Тестирование записи в файл, в строке указываем абсолютный путь, где будет файл
        InMemoryTaskManager manager = new InMemoryTaskManager();

        //Задачи для теста
        Epic epic = new Epic("epic", "desc");
        try {
            manager.createEpic(epic);
        } catch (TaskOverlapException e) {
            System.out.println(e.getMessage());
        }

//        SubTask subTask00 = new SubTask("Task00", "descrip", "15.03.2025 14:30", 120, epic);
//        try {
//            manager.createSubTask(subTask00);
//        } catch (TaskOverlapException e) {
//            System.out.println(e.getMessage());
//        }
//
//        SubTask subTask01 = new SubTask("Task01", "descrip", "15.03.2025 18:30", 100, epic);
//        try {
//            manager.createSubTask(subTask01);
//        } catch (TaskOverlapException e) {
//            System.out.println(e.getMessage());
//        }
//
//        try {
//            manager.updateSubTask(subTask01, null, null, null, "15.03.2025 14:30", 120L, epic);
//        } catch (TaskOverlapException e) {
//            System.out.println(e.getMessage());
//            System.out.println("не обновилось");
//        }

        System.out.println(manager.getAllTasksTypes());

    }
}

