package com.example.genetiicz;

import org.springframework.boot.SpringApplication;

public class TestGenetiiczApplication {

	public static void main(String[] args) {
		SpringApplication.from(GenetiiczApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
