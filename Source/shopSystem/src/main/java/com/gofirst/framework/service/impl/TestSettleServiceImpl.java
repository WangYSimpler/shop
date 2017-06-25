package com.gofirst.framework.service.impl;

import javax.inject.Named;

import com.gofirst.framework.service.TestSettleService;

@Named(value = "TestSettleService")
public class TestSettleServiceImpl implements TestSettleService {

	@Override
	public boolean SettleCalService() {
		System.out.println("abcd!!!");
		System.out.println("test Settle Service");
		return false;
	}

	

}
