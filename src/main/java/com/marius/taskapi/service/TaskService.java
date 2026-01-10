package com.marius.taskapi.service;

import com.marius.taskapi.dto.TaskRequest;
import com.marius.taskapi.dto.TaskResponse;
import com.marius.taskapi.exception.ResourceNotFoundException;
import com.marius.taskapi.exception.UnauthorizedException;
import com.marius.taskapi.model.Task;
import com.marius.taskapi.model.TaskPriority;
import com.marius.taskapi.model.TaskStatus;
import com.marius.taskapi.model.User;
import com.marius.taskapi.repository.TaskRepository;
import com.marius.taskapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    private User getCurrentUser(UserDetails userDetails) {
        String username = userDetails.getUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    
    private TaskResponse convertToResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setDueDate(task.getDueDate());
        response.setCategory(task.getCategory());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());
        response.setUserId(task.getUser().getId());
        return response;
    }
    
    @Transactional
    public TaskResponse createTask(TaskRequest request, UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : TaskStatus.TODO);
        task.setPriority(request.getPriority() != null ? request.getPriority() : TaskPriority.MEDIUM);
        task.setDueDate(request.getDueDate());
        task.setCategory(request.getCategory());
        task.setUser(user);
        
        task = taskRepository.save(task);
        return convertToResponse(task);
    }
    
    public List<TaskResponse> getAllTasks(UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        List<Task> tasks = taskRepository.findByUserId(user.getId());
        return tasks.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<TaskResponse> getTasksWithFilters(
            TaskStatus status,
            TaskPriority priority,
            String category,
            LocalDate dueDate,
            UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        List<Task> tasks = taskRepository.findByUserIdWithFilters(
                user.getId(), status, priority, category, dueDate);
        return tasks.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<TaskResponse> searchTasks(String keyword, UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        List<Task> tasks = taskRepository.searchByUserIdAndKeyword(user.getId(), keyword);
        return tasks.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public TaskResponse getTaskById(Long id, UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        
        if (!task.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You don't have permission to access this task");
        }
        
        return convertToResponse(task);
    }
    
    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest request, UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        
        if (!task.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You don't have permission to update this task");
        }
        
        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }
        if (request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }
        if (request.getCategory() != null) {
            task.setCategory(request.getCategory());
        }
        
        task = taskRepository.save(task);
        return convertToResponse(task);
    }
    
    @Transactional
    public void deleteTask(Long id, UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        
        if (!task.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You don't have permission to delete this task");
        }
        
        taskRepository.delete(task);
    }
}

