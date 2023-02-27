package com.driver.services.impl;

import com.driver.model.Cab;
import com.driver.model.Driver;
import com.driver.model.TripBooking;
import com.driver.model.TripStatus;
import com.driver.repository.CabRepository;
import com.driver.services.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.repository.DriverRepository;

import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {

	@Autowired
	DriverRepository driverRepository3;

	@Autowired
	CabRepository cabRepository3;

	@Override
	public void register(String mobile, String password) {
		//Save a driver in the database having given details and a cab with ratePerKm as 10 and availability as True by default.
		Driver driver = new Driver();
		Cab cab = new Cab();
		cab.setDriver(driver);
		cab.setAvailable(true);
		cab.setPerKmRate(10);

		driver.setCab(cab);
		driver.setMobile(mobile);
		driver.setPassword(password);
		driverRepository3.save(driver);
	}

	@Override
	public void removeDriver(int driverId) {
		// Delete driver without using deleteById function
		Driver driver = driverRepository3.findById(driverId).get();
		driverRepository3.delete(driver);
	}

	@Override
	public void updateStatus(int driverId) {
		//Set the status of respective car to unavailable
		Driver driver = driverRepository3.findById(driverId).get();
		List<TripBooking> trips = driver.getTripBookingList();
		Cab cab = driver.getCab();
		TripBooking lastTrip;
		if(trips.size() == 0){
			cab.setAvailable(true);
		}else{
			lastTrip = trips.get(trips.size() - 1);
			if(lastTrip.getStatus().equals(TripStatus.CONFIRMED)){
				cab.setAvailable(false);
			}else{
				cab.setAvailable(true);
			}
		}
		driverRepository3.save(driver);
	}
}
