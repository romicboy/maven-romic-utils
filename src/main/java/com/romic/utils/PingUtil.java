package com.romic.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PingUtil implements Runnable {

	private String ip;

	public PingUtil(String ip) {
		this.ip = ip;
	}

	public static void main(String[] args) {
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(100);
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 256; j++) {
				String ip = "64.233."+i+"."+j;
				fixedThreadPool.execute(new PingUtil(ip));
			}
		}
		fixedThreadPool.shutdown();
	}

	public void run() {
		try {
			boolean byName = InetAddress.getByName(ip).isReachable(3000);
			if (byName) {
				System.out.println(ip + " : " + byName);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
