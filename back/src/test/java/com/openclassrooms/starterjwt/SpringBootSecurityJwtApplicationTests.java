package com.openclassrooms.starterjwt;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SpringBootSecurityJwtApplicationTests {

	@Test
	void shouldRunMain() {
		SpringBootSecurityJwtApplication.main(new String[] {});
	}

}
