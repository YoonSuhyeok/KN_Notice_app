package com.deadline.knunotice.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Notices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 255, nullable = false)
    private String url;

    @Column(length = 255, nullable = false)
    private String date;

    @Column(length = 255, nullable = false)
    private Integer isPin;

    @Column(length = 255, nullable = false)
    private Integer isBookmark;

    @ManyToOne
    @JoinColumn(name = "major")
    private Majors majorFk;


    @Builder
    public Notices(String major){
        
    }

}
