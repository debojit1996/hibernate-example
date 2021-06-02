package com.debo.hibernateexample.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ToString
@Entity
public class Student {
    @Id
    private Long rollNumber;
    private String firstName;
    private String lastName;
    //OneToOne
    @ManyToMany
    private List<Laptop> laptops = new ArrayList<>();

    public Student(Long id, String firstName, String lastName) {
        this.rollNumber = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
