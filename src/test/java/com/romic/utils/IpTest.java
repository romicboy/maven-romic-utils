package com.romic.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class IpTest {

	@Test
	public void testGetAddressInfo() {
		Ip ip = new Ip("101.81.72.219");
		String addressInfo = ip.getAddressInfo();
		if (null == addressInfo) {
			fail("Not yet implemented");
		}
		System.out.println(addressInfo);
	}

}
