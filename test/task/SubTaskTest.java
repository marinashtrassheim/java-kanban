package task;

import management.Managers;
import management.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    private TaskManager taskManager;
    private Epic testEpic;
    private SubTask testSubTask;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        testEpic = new Epic("Test Epic", "Epic description");
        try {
            taskManager.createEpic(testEpic);
        } catch (exceptions.TaskOverlapException ignored) {}

        testSubTask = new SubTask(
                "Test SubTask",
                "SubTask description",
                "01.01.2023 10:00",
                60L,
                testEpic
        );
    }

    @Test
    void shouldCreateSubTaskWithCorrectParameters() {
        assertEquals("Test SubTask", testSubTask.getName());
        assertEquals("SubTask description", testSubTask.getDescription());
        assertEquals(Status.NEW, testSubTask.getStatus());
        assertEquals(testEpic, testSubTask.getEpic());
        assertEquals(LocalDateTime.of(2023, 1, 1, 10, 0), testSubTask.getStartTime());
        assertEquals(Duration.ofMinutes(60), testSubTask.getDuration());
    }

    @Test
    void shouldReturnCorrectEpic() {
        Epic newEpic = new Epic("New Epic", "New description");
        try {
            taskManager.createEpic(newEpic);
        } catch (exceptions.TaskOverlapException ignored) {}

        testSubTask.setEpic(newEpic);
        assertEquals(newEpic, testSubTask.getEpic());
        assertEquals(newEpic.getId(), testSubTask.getEpic().getId());
    }

    @Test
    void shouldCreateValidTempSubTask() {
        SubTask tempSubTask = testSubTask.createTempSubTask(
                testSubTask,
                "02.01.2023 12:00",
                90L
        );

        assertEquals(testSubTask.getId(), tempSubTask.getId());
        assertEquals(testSubTask.getName(), tempSubTask.getName());
        assertEquals(testSubTask.getEpic(), tempSubTask.getEpic());
        assertEquals(LocalDateTime.of(2023, 1, 2, 12, 0), tempSubTask.getStartTime());
        assertEquals(Duration.ofMinutes(90), tempSubTask.getDuration());
    }

    @Test
    void shouldReturnCorrectStringRepresentation() {
        testSubTask.setId(1);
        String expected = """
            Подзадача {
                ID = 1
                Название = 'Test SubTask'
                Описание = 'SubTask description'
                Статус = NEW
                ID эпика = %d
                Время начала = 01.01.2023 10:00
                Длительность = 60 минут
            }""".formatted(testEpic.getId());

        assertEquals(expected, testSubTask.toString());
    }
}