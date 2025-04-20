package test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import task.Epic;
import task.SubTask;

class EpicTest {
    private Epic epic1;
    private Epic epic2;
    private Epic epic3;
    private SubTask subTask;

    @BeforeEach
    void setUp() {
        //Создаем эпики с разными параметрами, но одинаковым id
        epic1 = new Epic("Epic1", "Description 1");
        epic1.setId(1);

        epic2 = new Epic("Epic2", "Description 2");
        epic2.setId(1);

        epic3 = new Epic("Epic3", "Description 3");
        epic3.setId(2);

        subTask = new SubTask("SubTask", "Description", epic1);
        subTask.setId(2);
    }

    @Test
    void epicsWithTheSameIdsShouldBeEquals() {
        assertEquals(epic1, epic2, "Эпики с одинаковым ID должны быть равны");
    }

    @Test
    public void testAddSubTask_EpicCannotBeItsOwnSubTask() {
        epic3.setSubtasks(subTask);
        assertTrue(epic3.getSubtasks().isEmpty());
    }
}