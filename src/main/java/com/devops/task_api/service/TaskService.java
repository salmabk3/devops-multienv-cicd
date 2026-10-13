package com.devops.task_api.service;

import com.devops.task_api.model.Task;
import com.devops.task_api.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository=taskRepository;
    }

    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task Not Found"));

    }

    public Task createTask(Task task){
        task.setId(null);
        task.setCreatedAt(java.time.LocalDateTime.now());
        if(task.getStatus() == null){
            task.setStatus("TODO");
        }

        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updatedTask){
        Task task = getTaskById(id);

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setStatus(updatedTask.getStatus());

        return taskRepository.save(task);
    }

    public void deleteTask(Long id){
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }

}
