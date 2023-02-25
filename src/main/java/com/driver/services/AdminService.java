package com.driver.services;

import java.util.List;

import com.driver.model.Admin;
import com.driver.model.Customer;
import com.driver.model.Driver;

public interface AdminService {

	public void adminRegister(Admin admin) throws Exception;

	public Admin updatePassword(Integer adminId, String password) throws Exception;

	public void deleteAdmin(int adminId) throws Exception;

	public List<Driver> getListOfDrivers() throws Exception;
	
	public List<Customer> getListOfCustomers() throws Exception;
}
