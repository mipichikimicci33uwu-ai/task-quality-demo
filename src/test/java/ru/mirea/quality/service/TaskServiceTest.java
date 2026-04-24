package ru.mirea.quality.service;

import org.junit.jupiter.api.Test;
import ru.mirea.quality.model.Task;
import ru.mirea.quality.model.TaskStatus;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskServiceTest {

    @Test
    void createTaskShouldCreateTaskWithTodoStatus() {
        TaskService service = new TaskService();

        Task task = service.createTask("Write tests", "Add unit tests for service");

        assertEquals(1L, task.getId());
        assertEquals("Write tests", task.getTitle());
        assertEquals(TaskStatus.TODO, task.getStatus());
        assertEquals(1, service.findAll().size());
    }

    @Test
    void updateStatusShouldChangeTaskStatus() {
        TaskService service = new TaskService();
        Task task = service.createTask("Create CI config", "Configure Travis CI");

        Task updated = service.updateStatus(task.getId(), TaskStatus.DONE);

        assertEquals(TaskStatus.DONE, updated.getStatus());
        assertEquals(TaskStatus.DONE, service.findById(task.getId()).orElseThrow().getStatus());
    }

    @Test
    void updateStatusShouldThrowExceptionForUnknownTask() {
        TaskService service = new TaskService();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.updateStatus(100L, TaskStatus.DONE)
        );

        assertTrue(exception.getMessage().contains("100"));
    }

    @Test
    void findByStatusShouldReturnOnlyTasksWithRequestedStatus() {
        TaskService service = new TaskService();
        Task first = service.createTask("First", "First task");
        Task second = service.createTask("Second", "Second task");
        service.updateStatus(second.getId(), TaskStatus.IN_PROGRESS);

        List<Task> todoTasks = service.findByStatus(TaskStatus.TODO);

        assertEquals(1, todoTasks.size());
        assertEquals(first.getId(), todoTasks.get(0).getId());
    }

    @Test
    void statisticsShouldContainCounterForEachStatus() {
        TaskService service = new TaskService();
        Task first = service.createTask("First", "First task");
        Task second = service.createTask("Second", "Second task");
        service.createTask("Third", "Third task");
        service.updateStatus(first.getId(), TaskStatus.DONE);
        service.updateStatus(second.getId(), TaskStatus.IN_PROGRESS);

        Map<TaskStatus, Long> statistics = service.getStatusStatistics();

        assertEquals(1L, statistics.get(TaskStatus.TODO));
        assertEquals(1L, statistics.get(TaskStatus.IN_PROGRESS));
        assertEquals(1L, statistics.get(TaskStatus.DONE));
        assertFalse(statistics.isEmpty());
    }
}
