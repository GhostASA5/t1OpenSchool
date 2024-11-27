package com.project.t1_openSchool.model;

import jakarta.persistence.*;
import lombok.*;

import java.text.MessageFormat;


@Entity
@Table(name = "task")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column(name = "user_id")
    private Long userId;

    @Override
    public String toString() {
        return MessageFormat
                .format("Task: id - {0}, title - {1}, description - {2}, userId - {3}",
                        id,
                        title,
                        description,
                        userId);
    }
}
