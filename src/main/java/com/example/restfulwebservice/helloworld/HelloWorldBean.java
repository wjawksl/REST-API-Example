package com.example.restfulwebservice.helloworld;
import lombok.AllArgsConstructor;
import lombok.Data;

//lombok 추가 했었음
@Data // Getter Setter 자동 생성 어노테이션
@AllArgsConstructor // 생성자 자동 생성 어노테이션
public class HelloWorldBean {

    private String message;
}
