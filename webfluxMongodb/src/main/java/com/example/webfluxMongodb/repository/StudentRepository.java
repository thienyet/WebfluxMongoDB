package com.example.webfluxMongodb.repository;

import com.example.webfluxMongodb.model.Student;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface StudentRepository extends ReactiveMongoRepository<Student, String> {
}
