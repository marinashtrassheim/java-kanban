package management;

import task.*;

public final class CSVFormat {

    public static String taskToCSVString(Task task) {
        TaskType taskType;
        String epicId = "";

        if (task instanceof Epic) {
            taskType = TaskType.EPIC;
        } else if (task instanceof SubTask) {
            taskType = TaskType.SUBTASK;
            epicId = String.valueOf(((SubTask) task).getEpic().getId());
        } else {
            taskType = TaskType.TASK;
        }

        return String.join(",",
                String.valueOf(task.getId()),
                String.valueOf(taskType),
                task.getName(),
                task.getStatus().name(),
                task.getDescription(),
                epicId);
    }

    public static Task taskFromString(FileBackedTaskManager manager, String value) {
        String[] parts = value.split(",", -1);
        int id = Integer.parseInt(parts[0].trim());
        TaskType taskType = TaskType.valueOf(parts[1].trim());
        String name = parts[2].trim();
        Status status = Status.valueOf(parts[3].trim());
        String description = parts[4].trim();

        if (taskType == TaskType.TASK) {
            Task task = new Task(name, description, status);
            task.setId(id);
            return task;
        } else if (taskType == TaskType.EPIC) {
            Epic epic = new Epic(name, description);
            epic.setId(id);
            epic.setStatus(status);
            return epic;
        } else {
            int epicId = Integer.parseInt(parts[5].trim());
            Epic epic = manager.getAllEpics().get(epicId);
            SubTask subTask = new SubTask(name, description, epic);
            subTask.setId(id);
            subTask.setStatus(status);
            return subTask;
        }

    }

}
