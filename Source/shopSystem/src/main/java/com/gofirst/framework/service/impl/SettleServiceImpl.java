package com.gofirst.framework.service.impl;

import javax.inject.Named;

import com.gofirst.framework.service.SettleService;

@Named(value = "settleService")
public class SettleServiceImpl implements  SettleService{

	@Override
	public boolean TestSettleServiceFunc() {
		
		System.out.println("Test Settle Serive Function");
		return true;
	}


}
