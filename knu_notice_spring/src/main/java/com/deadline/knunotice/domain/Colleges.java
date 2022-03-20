package com.deadline.knunotice.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Colleges {

    @Id
    @GeneratedValue
    @Column(length = 255, nullable = false)
    private String college;

    @Builder
    public Colleges(String college, String major){
        this.college = college;
    }

}
