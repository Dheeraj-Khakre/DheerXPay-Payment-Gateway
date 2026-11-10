package com.dheerx.DheerX_Pay;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.dheerx.DheerX_Pay.DheerXPayApplication;

class DheerXPayApplicationTests {

	@Test
	void applicationExposesMainEntryPoint() throws NoSuchMethodException {
		assertNotNull(DheerXPayApplication.class.getDeclaredMethod("main", String[].class));
	}

}
