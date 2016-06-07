package com.romic.utils;

import org.junit.Test;

public class LogerTest {

	@Test
	public void testInfoStringString() {
		Loger.info("123456");
	}

	@Test
	public void testInfoString() {
		Loger.info("domain","123456");
	}

}
