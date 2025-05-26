package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
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
    private LocalDateTime epicStartTime; // Время старта эпика (=времени старта 1 задачи в subtasks) (2023-11-15T14:30)
    private Duration epicDuration; // Длительность работы над эпиком (сумма duration всех subtasks)
    private LocalDateTime epicEndTime; // Время окончания эпика (=времени окончания 1 задачи в subtasks) (2023-11-15T14:30)

    /**
     * Конструктор для создания эпика, когда список подзадач неизвестен.
     *
     * @param name        Название эпика
     * @param description Описание эпика
     */
    public Epic(String name, String description) {
        super(name, description);
        this.status = Status.NEW; // По умолчанию статус NEW
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
        this.epicStartTime = epicStartTime;
    }

    public LocalDateTime getEpicStartTime() {
        return epicStartTime;
    }

    public void setEpicEndTime(LocalDateTime epicEndTime) {
        this.epicEndTime = epicEndTime;
    }

    public LocalDateTime getEpicEndTime() {
        return this.epicEndTime;
    }

    public void setEpicDuration(Duration duration) {
        this.epicDuration = duration;
    }

    public Duration getEpicDuration() {
        return epicDuration;
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
                epicStartTime != null ? epicStartTime.format(formatter) : "не назначено",
                epicDuration != null ? epicDuration.toMinutes() : "не назначено");
    }
}
