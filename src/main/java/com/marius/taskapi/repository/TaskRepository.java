package com.marius.taskapi.repository;

import com.marius.taskapi.model.Task;
import com.marius.taskapi.model.TaskPriority;
import com.marius.taskapi.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findByUserId(Long userId);
    
    List<Task> findByUserIdAndStatus(Long userId, TaskStatus status);
    
    List<Task> findByUserIdAndPriority(Long userId, TaskPriority priority);
    
    List<Task> findByUserIdAndCategory(Long userId, String category);
    
    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:priority IS NULL OR t.priority = :priority) AND " +
           "(:category IS NULL OR t.category = :category) AND " +
           "(:dueDate IS NULL OR t.dueDate = :dueDate)")
    List<Task> findByUserIdWithFilters(
        @Param("userId") Long userId,
        @Param("status") TaskStatus status,
        @Param("priority") TaskPriority priority,
        @Param("category") String category,
        @Param("dueDate") LocalDate dueDate
    );
    
    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND " +
           "(LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Task> searchByUserIdAndKeyword(@Param("userId") Long userId, @Param("keyword") String keyword);
}

