package com.debo.hibernateexample.init;

import com.debo.hibernateexample.entity.Laptop;
import com.debo.hibernateexample.entity.Student;
import com.debo.hibernateexample.repository.LaptopRepository;
import com.debo.hibernateexample.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;

@Service
@Slf4j
@Transactional
public class DataSetup implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private LaptopRepository laptopRepository;

    @Override
    public void run(String... args) {
        var student = new Student(1L, "Debo", "Chakraborty");
        var laptop = new Laptop("Dell");
        var laptop2 = new Laptop("Lenovo");

        this.laptopRepository.save(laptop);
        this.laptopRepository.save(laptop2);

        student.setLaptops(Arrays.asList(laptop, laptop2));
        this.studentRepository.save(student);
        log.info("-------------------------------------------");
        log.info("Fetching student by Id");
        log.info("-------------------------------------------");

        Student student1 = studentRepository.findById(1L).get();

        for(Laptop l: student1.getLaptops()) {
            System.out.println(l);
        }
        System.out.println("The student object: "+student1);



    }
}
