package com.debo.hibernateexample.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ToString
@Entity
public class Laptop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    // Since we have referred ManyToMany in both Student and Laptop classes (both are individual entities),
    // if we don't do this mappedBy in one of the entities, we will have 4 tables named Laptop, Student,
    // Laptop_Student and Student_Laptop. To restrict this to 3 tables, we use this mappedBy in any of the
    // 2 classes( in case of ManyToMany or OneToMany). In below example, we ask to Student to create
    // Student_Laptop since we refer to the List<Laptop> laptops variable of that class.
    @ManyToMany(mappedBy = "laptops")
    private List<Student> students = new ArrayList<>();

    public Laptop(String name) {
        this.name = name;
    }

}
