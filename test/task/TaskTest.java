package task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class TaskTest {

    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        // Создаем задачи с разными параметрами, но одинаковыми ID
        task1 = new Task("Task 1", "Description 1");
        task1.setId(1);  // Устанавливаем ID вручную для тестов

        task2 = new Task("Task 2", "Description 2");
        task2.setId(1);  // Такой же ID как у task1

    }

    @Test
    void tasksWithSameIdShouldBeEqual() {
        assertEquals(task1, task2, "Задачи с одинаковым ID должны быть равны");
    }
}