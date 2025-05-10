package test;

import management.Managers;
import management.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    private SubTask subTask1;
    private SubTask subTask2;
    private Epic epic;
    TaskManager taskManager = Managers.getDefault();

    @BeforeEach
    void setUp() {
        //Создаем эпик для подзадач
        epic = new Epic("Epic1", "Description 1");
        taskManager.createEpic(epic);
        //Создаем подзадачи с разными параметрами, но одинаковым id
        subTask1 = new SubTask("SubTask1", "Description 1", epic);
        subTask1.setId(1);

        subTask2 = new SubTask("SubTask2", "Description 2", epic);
        subTask2.setId(1);
    }

    @Test
    void epicsWithTheSameIdsShouldBeEquals() {
        assertEquals(subTask1, subTask2, "Подзадачи с одинаковым ID должны быть равны");
    }

    @Test
    public void testSetEpic_SubTaskCannotBeItsOwnEpic() {
        // Создаем эпик
        Epic epic = new Epic("Epic 1", "Description for Epic 1");
        epic.setId(1); // Устанавливаем ID эпика

        // Создаем подзадачу с таким же ID, как у эпика
        SubTask subTask = new SubTask("SubTask 1", "Description for SubTask 1", epic);
        subTask.setId(1); // Устанавливаем такой же ID, как у эпика

        // Создаем еще один эпик с таким же ID (имитируем попытку сделать подзадачу своим эпиком)
        Epic sameIdEpic = new Epic("Same ID Epic", "Same ID Description");
        sameIdEpic.setId(1); // ID совпадает с подзадачей

        // Пытаемся установить эпик с таким же ID, как у подзадачи
        subTask.setEpic(sameIdEpic); // Должно проигнорироваться, т.к. id совпадают

        // Проверяем, что эпик подзадачи остался исходным, а не изменился на sameIdEpic
        assertEquals(epic, subTask.getEpic(), "Эпик не должен измениться, если ID совпадает с подзадачей");
        assertNotSame(sameIdEpic, subTask.getEpic(), "Подзадача не должна принимать эпик с таким же ID");
    }
}