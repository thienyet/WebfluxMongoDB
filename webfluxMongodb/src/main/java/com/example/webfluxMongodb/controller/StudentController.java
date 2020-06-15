package com.example.webfluxMongodb.controller;

import com.example.webfluxMongodb.model.Student;
import com.example.webfluxMongodb.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.RedirectView;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping(value="/students")
    public String findAllStudents(Model model) {
        Flux<Student> listStudent = studentRepository.findAll();
        model.addAttribute("students", listStudent);
        return "index";
    }

    @GetMapping(value="/students/edit/{id}")
    public String findStudentById(@PathVariable(value="id") String id, Model model) {
        Mono<Student> student = studentRepository.findById(id);
        model.addAttribute("student", student);
        return "edit-student";
    }

    @PostMapping(value = "/students/edit/{id}")
    public RedirectView updateStudent(@PathVariable(value = "id") String id, @ModelAttribute Student student) {
        Mono<Student> student1 = studentRepository.save(student);
        student1.subscribe(
                i -> System.out.println(i)
        );
        return new RedirectView("/students", HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping(value = "/students/add-student")
    public String createStudent() {
        return "add-student";
    }

    @PostMapping(value = "/students/add-student")
    public RedirectView postCreateStudent (@ModelAttribute Student student, Model model) {
        studentRepository.insert(student)
                .map(st -> ResponseEntity.ok(st))
                .subscribe(i -> System.out.println(i));
        Flux<Student> listStudent = studentRepository.findAll();
        model.addAttribute("students", listStudent);
        return new RedirectView("/students", HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping(value = "/students/delete/{id}")
    public Mono<ResponseEntity<Void>> deleteStudent(@PathVariable(value = "id") String id){
        return studentRepository.findById(id)
                .flatMap(i ->
                        studentRepository.delete(i)
                                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

//    @GetMapping(value = "/students/delete/{id}")
//    public String deleteStudent(@PathVariable(value = "id") String id, Model model){
//        studentRepository.deleteById(id);
//        Flux<Student> listStudent = studentRepository.findAll();
//        model.addAttribute("students", listStudent);
//        return "index";
//    }

}

