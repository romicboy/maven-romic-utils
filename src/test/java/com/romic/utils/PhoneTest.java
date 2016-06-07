package com.romic.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class PhoneTest {

	@Test
	public void testGetInfo() {
		Phone phone = new Phone("18521050419");
		String info = phone.getInfo();
		if (null == info) {
			fail("Not yet implemented");
		}
		System.out.println(info);
	}

}
