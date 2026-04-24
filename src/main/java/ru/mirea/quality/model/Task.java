package ru.mirea.quality.model;

import java.time.LocalDate;
import java.util.Objects;

public class Task {
    private final long id;
    private final String title;
    private final String description;
    private final LocalDate createdAt;
    private TaskStatus status;

    public Task(long id, String title, String description, LocalDate createdAt) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Task title must not be empty");
        }
        this.id = id;
        this.title = title;
        this.description = description == null ? "" : description;
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
        this.status = TaskStatus.TODO;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = Objects.requireNonNull(status, "status must not be null");
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                '}';
    }
}
