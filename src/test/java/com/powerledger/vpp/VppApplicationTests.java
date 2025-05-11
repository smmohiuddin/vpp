package com.powerledger.vpp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VppApplicationTests {

	@Test
	void applicationStartsSuccessfully() {
		String[] args = {};
		VppApplication.main(args);
	}

}
