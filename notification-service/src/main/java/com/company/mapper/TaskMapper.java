package com.company.mapper;

import com.company.entity.TaskEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Mapper
@Repository
public interface TaskMapper {
    @Select("SELECT * FROM tasks " +
            "WHERE date = #{date} " +
            "AND is_done = false")
    List<TaskEntity> showAllTasksAtDay(LocalDate date);
}
