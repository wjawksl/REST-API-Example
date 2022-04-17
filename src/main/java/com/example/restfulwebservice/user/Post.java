package com.example.restfulwebservice.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    //User : Post -> 1:N (0~N) Main : Sub  -> parent : child
    @ManyToOne(fetch = FetchType.LAZY) // 현재 Post라는 데이터가 여러 개 존재 가능, User 하나로 연결된다
    @JsonIgnore
    private User user;
}
