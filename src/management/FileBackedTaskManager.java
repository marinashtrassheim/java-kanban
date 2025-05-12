package management;

import exceptions.ManagerSaveException;
import task.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    public final Path filePath;

    public FileBackedTaskManager(String filePath) {
        this.filePath = Paths.get(filePath).toAbsolutePath();
    }

    private void save() {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Failed to save tasks to file: " + filePath, e);
        }

        try (BufferedWriter fileWriter = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            fileWriter.write("id,type,name,status,description,epic" + "\n");
            for (Task task : getAllTasksTypes().values()) {
                fileWriter.write(CSVFormat.taskToCSVString(task) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Failed to save tasks to file: " + filePath, e);
        }

    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager loadManager = new FileBackedTaskManager(file.getAbsolutePath());

        try {
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.isEmpty()) {
                    continue;
                }
                Task task = CSVFormat.taskFromString(loadManager, line);
                loadManager.getAllTasksTypes().put(task.getId(), task);
            }

            for (SubTask subTask : loadManager.getAllSubTasks().values()) {
                Epic epic = loadManager.getAllEpics().get(subTask.getEpic().getId());
                if (epic != null) {
                    epic.setSubtasks(subTask);
                }
            }
            return loadManager;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки из файла", e);
        }
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteAll() {
        super.deleteAll();
        save();
    }

    @Override
    public void updateTask(Task currentTask, Task updatedTask) {
        super.updateTask(currentTask, updatedTask);
        save();
    }

    @Override
    public void updateSubTask(SubTask currentSubTask, SubTask updatedSubTask) {
        super.updateSubTask(currentSubTask, updatedSubTask);
        save();
    }

    @Override
    public void updateEpic(Epic currentEpic, Epic updatedEpic) {
        super.updateEpic(currentEpic, updatedEpic);
        save();
    }

    @Override
    public void addSubtaskToEpic(Epic epic, SubTask subTask) {
        super.addSubtaskToEpic(epic, subTask);
        save();
    }


}
