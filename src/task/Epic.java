package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;

/**
 * Класс Epic представляет собой задачу типа "Эпик", которая может содержать подзадачи (SubTask).
 * Эпик имеет статус, который зависит от статусов его подзадач.
 */
public class Epic extends Task {
    // Список подзадач, относящихся к этому эпику
    private final TreeSet<SubTask> prioritizedSubTasks = new TreeSet<>(
            Comparator.comparing(SubTask::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(SubTask::getId)
    );
    private LocalDateTime epicEndTime; // Время окончания эпика (=времени окончания 1 задачи в subtasks) (2023-11-15T14:30)

    /**
     * Конструктор для создания эпика, когда список подзадач неизвестен.
     *
     * @param name        Название эпика
     * @param description Описание эпика
     */
    public Epic(String name, String description) {
        super(name, description);
        epicEndTime = LocalDateTime.now();
    }

    /**
     * Метод для обновления статуса эпика на основе статусов его подзадач.
     * Логика обновления:
     * 1. Если список подзадач пуст или все подзадачи имеют статус NEW, то статус эпика — NEW.
     * 2. Если все подзадачи имеют статус DONE, то статус эпика — DONE.
     * 3. В остальных случаях статус эпика — IN_PROGRESS.
     */
    public void updateEpicStatus(Epic epic) {
        if (prioritizedSubTasks.isEmpty()) {
            epic.setStatus(Status.NEW); // Если подзадач нет, статус NEW
            return;
        }

        boolean allDone = true; // Предполагаем, что все подзадачи завершены
        boolean allNew = true;  // Предполагаем, что все подзадачи новые
        // Проверяем статусы всех подзадач
        for (SubTask subTask : prioritizedSubTasks) {
            if (subTask.getStatus() != Status.DONE) {
                allDone = false; // Если хотя бы одна подзадача не DONE, то Epic не DONE
            }
            if (subTask.getStatus() != Status.NEW) {
                allNew = false; // Если хотя бы одна подзадача не NEW, то Epic не NEW
            }
        }
        // Устанавливаем статус эпика на основе проверки
        if (allDone) {
            epic.setStatus(Status.DONE);
        } else if (allNew) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    public void updateEpicTimes(Epic epic) {
        if (prioritizedSubTasks.isEmpty()) {
            epic.setEpicStartTime(this.startTime);
            epic.setEpicEndTime(this.epicEndTime);
            return;
        }

        SubTask first = prioritizedSubTasks.first();
        epic.setEpicStartTime(first.getStartTime());

        SubTask last = prioritizedSubTasks.last();
        epic.setEpicEndTime(last.getEndTime());

        Duration duration = prioritizedSubTasks.stream()
                .map(SubTask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);
        epic.setEpicDuration(duration);
    }

    public void setPrioritizedSubTasks(SubTask subTask) {
        prioritizedSubTasks.add(subTask);
    }

    public void deletePrioritizedSubTasks(SubTask subTask) {
        prioritizedSubTasks.remove(subTask);
    }

    public TreeSet<SubTask> getPrioritizedSubTasks() {
        return prioritizedSubTasks;
    }

    public void setEpicStartTime(LocalDateTime epicStartTime) {
        this.startTime= epicStartTime;
    }

    public LocalDateTime getEpicStartTime() {
        return startTime;
    }

    public void setEpicEndTime(LocalDateTime epicEndTime) {
        this.epicEndTime = epicEndTime;
    }

    public LocalDateTime getEpicEndTime() {
        return this.epicEndTime;
    }

    public void setEpicDuration(Duration duration) {
        this.duration= duration;
    }

    public Duration getEpicDuration() {
        return duration;
    }

    public Optional<SubTask> getSubtaskById(int id) {
        return prioritizedSubTasks.stream()
                .filter(subTask -> subTask.getId() == id)
                .findFirst();
    }

    /**
     * Переопределение метода toString для удобного вывода информации об эпике.
     *
     * @return Строковое представление эпика в формате:
     * "Эпик {ID=..., Название=..., Описание=..., Статус=..., Подзадачи=...}"
     */
    @Override
    public String toString() {
        return String.format("""
                        Эпик {
                            ID = %d
                            Название = '%s'
                            Описание = '%s'
                            Статус = %s
                            Подзадачи = %s
                            Время начала = %s
                            Длительность = %s минут
                        }""",
                id,
                name,
                description,
                status,
                prioritizedSubTasks,
                startTime != null ? startTime.format(formatter) : "не назначено",
                duration != null ? duration.toMinutes() : "не назначено");
    }
}
