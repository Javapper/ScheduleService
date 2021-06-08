package com.company.mapper;

import com.company.entity.TaskEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Mapper
@Repository
public interface TaskMapper {
    @Select("SELECT * FROM tasks " +
            "WHERE task_id = #{taskId}")
    TaskEntity showTaskById(long taskId);

    @Select("SELECT * FROM tasks")
    List<TaskEntity> showAllTasks();

    @Select("SELECT * FROM tasks " +
            "WHERE date = #{date}")
    List<TaskEntity> showAllTasksAtDay(LocalDate date);

    @Insert("INSERT INTO tasks (task, date, is_done) " +
            "VALUES (#{task}, #{date}, false)")
    void addTask(TaskEntity taskEntity);

    @Delete("DELETE FROM tasks " +
            "WHERE task_id = #{taskId}")
    void deleteTask(int taskId);

    @Update("UPDATE tasks " +
            "SET  task = #{task} " +
            "WHERE task_id = #{taskId}")
    void updateTask(TaskEntity taskEntity);

    @Update("UPDATE tasks " +
            "SET date = #{date} " +
            "WHERE task_id = #{taskId}")
    void rescheduleTask(TaskEntity taskEntity);

    @Update("UPDATE tasks " +
            "SET is_done = true " +
            "WHERE task_id = #{taskId}")
    void makeTaskDone(int taskId);

    @Update("UPDATE tasks " +
            "SET is_done = false " +
            "WHERE task_id = #{taskId}")
    void makeTaskUndone(int taskId);
}

INSERT INTO tasks (task, date, is_done) VALUES ('do', '2021-05-05', false);