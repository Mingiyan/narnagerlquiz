package ru.halmg.narnagerl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_tbl")
public class User {

    @Id
    @GeneratedValue
    private Long userId;

    @Column(name = "chat")
    private String chatId;

    @Column(name = "level", nullable = false)
    @Enumerated(EnumType.STRING)
    private Level level;
}
