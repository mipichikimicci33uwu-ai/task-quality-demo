package ru.mirea.quality;

import ru.mirea.quality.model.Task;
import ru.mirea.quality.model.TaskStatus;
import ru.mirea.quality.service.TaskService;

public class DemoApplication {
    public static void main(String[] args) {
        TaskService service = new TaskService();

        Task documentation = service.createTask("Prepare report", "Write a short report for practice work");
        Task testing = service.createTask("Add tests", "Cover the service logic with unit tests");

        service.updateStatus(documentation.getId(), TaskStatus.IN_PROGRESS);
        service.updateStatus(testing.getId(), TaskStatus.DONE);

        System.out.println("All tasks:");
        service.findAll().forEach(System.out::println);
        System.out.println("Statistics: " + service.getStatusStatistics());
    }
}
