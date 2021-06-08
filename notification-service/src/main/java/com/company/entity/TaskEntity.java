package com.company.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {
    private long taskId;
    private String task;
    private LocalDate date;
    private boolean isDone;
}
