package com.tb.api.tbapiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@EnableEncryptableProperties
public class TbapiserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(TbapiserverApplication.class, args);
	}

}
