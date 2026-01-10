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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    
    @Mock
    private TaskRepository taskRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private UserDetails userDetails;
    
    @InjectMocks
    private TaskService taskService;
    
    private User user;
    private Task task;
    private TaskRequest taskRequest;
    
    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.MEDIUM);
        task.setUser(user);
        
        taskRequest = new TaskRequest();
        taskRequest.setTitle("New Task");
        taskRequest.setDescription("New Description");
        taskRequest.setStatus(TaskStatus.TODO);
        taskRequest.setPriority(TaskPriority.HIGH);
        
        when(userDetails.getUsername()).thenReturn("testuser");
    }
    
    @Test
    void testCreateTask_Success() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        
        // Act
        TaskResponse response = taskService.createTask(taskRequest, userDetails);
        
        // Assert
        assertNotNull(response);
        verify(taskRepository, times(1)).save(any(Task.class));
    }
    
    @Test
    void testGetAllTasks_Success() {
        // Arrange
        List<Task> tasks = Arrays.asList(task);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(taskRepository.findByUserId(anyLong())).thenReturn(tasks);
        
        // Act
        List<TaskResponse> responses = taskService.getAllTasks(userDetails);
        
        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(taskRepository, times(1)).findByUserId(anyLong());
    }
    
    @Test
    void testGetTaskById_Success() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        
        // Act
        TaskResponse response = taskService.getTaskById(1L, userDetails);
        
        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Task", response.getTitle());
    }
    
    @Test
    void testGetTaskById_NotFound() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.getTaskById(1L, userDetails);
        });
    }
    
    @Test
    void testGetTaskById_Unauthorized() {
        // Arrange
        User otherUser = new User();
        otherUser.setId(2L);
        task.setUser(otherUser);
        
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        
        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> {
            taskService.getTaskById(1L, userDetails);
        });
    }
    
    @Test
    void testUpdateTask_Success() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        
        // Act
        TaskResponse response = taskService.updateTask(1L, taskRequest, userDetails);
        
        // Assert
        assertNotNull(response);
        verify(taskRepository, times(1)).save(any(Task.class));
    }
    
    @Test
    void testDeleteTask_Success() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(any(Task.class));
        
        // Act
        taskService.deleteTask(1L, userDetails);
        
        // Assert
        verify(taskRepository, times(1)).delete(any(Task.class));
    }
}

