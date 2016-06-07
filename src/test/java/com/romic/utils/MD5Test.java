package com.romic.utils;

import static org.junit.Assert.fail;

import org.junit.Test;

public class MD5Test {

	@Test
	public void testMD5EncodeStringString() {
		String str = MD5.Encode("123456");
		if (str.equals("")) {
			fail("Not yet implemented");
		}
		System.out.println(str);
	}

	@Test
	public void testMD5EncodeString() {
		String str = MD5.Encode("123456", "UTF-8");
		if (str.equals("")) {
			fail("Not yet implemented");
		}
		System.out.println(str);
	}

}
