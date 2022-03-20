package com.deadline.knunotice.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Majors {

    @Id
    @GeneratedValue
    @Column(length = 255, nullable = false)
    private String major;

    @ManyToOne
    @JoinColumn(name = "college")
    private Colleges collegesFk;

    @Builder
    public Majors(String major){
        this.major = major;
    }
}
