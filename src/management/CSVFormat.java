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
            Epic epic = ((SubTask) task).getEpic();
            epicId = String.valueOf(epic.getId());
        } else {
            taskType = TaskType.TASK;
        }

        String startTimeStr;
        try {
            if (task.getStartTime() != null) {
                startTimeStr = task.getStartTime().format(task.getFormatter());
            } else {
                startTimeStr = "";
            }
        } catch (IllegalStateException e) {
            startTimeStr = "";
        }

        String duration;
        try {
            if (task.getDuration() != null) {
                duration = String.valueOf(task.getDuration().toMinutes());
            } else {
                duration = "";
            }
        } catch (IllegalStateException e) {
            duration = "";
        }


        return String.join(",",
                String.valueOf(task.getId()),
                String.valueOf(taskType),
                task.getName(),
                task.getStatus().name(),
                task.getDescription(),
                startTimeStr,
                duration,
                epicId);
    }

    public static Task taskFromString(FileBackedTaskManager manager, String value) {
        String[] parts = value.split(",", -1);
        int id = Integer.parseInt(parts[0].trim());
        TaskType taskType = TaskType.valueOf(parts[1].trim());
        String name = parts[2].trim();
        Status status = Status.valueOf(parts[3].trim());
        String description = parts[4].trim();

        String startTime = null;
        if (!parts[5].trim().isEmpty()) {
            try {
                startTime = parts[5].trim();
            } catch (IllegalStateException ignored) {
            }
        }

        // Обработка duration (может быть пустым)
        long duration = 0;
        if (!parts[6].trim().isEmpty()) {
            try {
                duration = Long.parseLong(parts[6].trim());
            } catch (IllegalStateException ignored) {
            }
        }


        if (taskType == TaskType.TASK) {
            Task task;
            if (startTime != null && duration != 0) {
                task = new Task(name, description, startTime, duration, status);
            } else {
                task = new Task(name, description, status);
            }
            task.setId(id);
            return task;
        } else if (taskType == TaskType.EPIC) {
            Epic epic = new Epic(name, description);
            epic.setId(id);
            epic.setStatus(status);
            return epic;
        } else {
            int epicId = Integer.parseInt(parts[7].trim());
            Epic epic = manager.getAllEpics().get(epicId);
            SubTask subTask;
            if (startTime != null && duration != 0) {
                subTask = new SubTask(name, description, startTime, duration, epic);
            } else {
                subTask = new SubTask(name, description, epic);
            }
            subTask.setId(id);
            subTask.setStatus(status);
            return subTask;
        }

    }

}
