package com.driver.services;

import com.driver.model.Driver;

public interface DriverService {

		public void register(String mobile, String password) throws Exception;
		public void removeDriver(int driverId) throws Exception;
		public void updateStatus(int driverId) throws Exception;
}
