package com.example.schoolportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;
import java.io.File;

@SpringBootApplication
public class SchoolportalApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolportalApplication.class, args);
	}

	@PostConstruct
	public void initUploadsDir() {
		File uploadDir = new File("uploads");
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
			System.out.println("Uploads directory created at: " + uploadDir.getAbsolutePath());
		} else {
			System.out.println("Uploads directory already exists at: " + uploadDir.getAbsolutePath());
		}
	}
}
