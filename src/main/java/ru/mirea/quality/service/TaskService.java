package ru.mirea.quality.service;

import ru.mirea.quality.model.Task;
import ru.mirea.quality.model.TaskStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class TaskService {
    private final AtomicLong idSequence = new AtomicLong(1);
    private final List<Task> tasks = new ArrayList<>();

    public Task createTask(String title, String description) {
        Task task = new Task(idSequence.getAndIncrement(), title, description, LocalDate.now());
        tasks.add(task);
        return task;
    }

    public Optional<Task> findById(long id) {
        return tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst();
    }

    public Task updateStatus(long id, TaskStatus newStatus) {
        Task task = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task with id " + id + " was not found"));
        task.setStatus(newStatus);
        return task;
    }

    public List<Task> findByStatus(TaskStatus status) {
        return tasks.stream()
                .filter(task -> task.getStatus() == status)
                .sorted(Comparator.comparing(Task::getCreatedAt).thenComparing(Task::getId))
                .collect(Collectors.toList());
    }

    public List<Task> findAll() {
        return Collections.unmodifiableList(tasks);
    }

    public Map<TaskStatus, Long> getStatusStatistics() {
        Map<TaskStatus, Long> statistics = new EnumMap<>(TaskStatus.class);
        for (TaskStatus status : TaskStatus.values()) {
            statistics.put(status, 0L);
        }
        for (Task task : tasks) {
            statistics.put(task.getStatus(), statistics.get(task.getStatus()) + 1);
        }
        return statistics;
    }
}
