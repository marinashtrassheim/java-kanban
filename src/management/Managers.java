package management;

public final class Managers {
    private Managers() {} // Запрещаем создание экземпляров

    /**
     * Возвращает реализацию TaskManager по умолчанию.
     * Сейчас это InMemoryTaskManager, но может измениться в будущем.
     */
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}