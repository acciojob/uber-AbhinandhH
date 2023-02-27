package com.driver.services.impl;

import com.driver.model.TripBooking;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.model.Customer;
import com.driver.model.Driver;
import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;
import com.driver.model.TripStatus;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		customerRepository2.deleteById(customerId);
	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm)  throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		TripBooking newTrip = new TripBooking();
		Customer customer = customerRepository2.findById(customerId).get();;
		List<Driver> driverList = driverRepository2.findAll();
		Driver driverAvailable = null;
		for(Driver driver : driverList){
			if(driver.getCab().getAvailable() == true){
				driverAvailable = driver;
				break;
			}
		}
		if(driverAvailable == null){
			throw new Exception("No cab available!");
		}
		int ratePerKm = driverAvailable.getCab().getPerKmRate();
		int totalBill = ratePerKm * distanceInKm;
		newTrip.setBill(totalBill);
		newTrip.setDistanceInKm(distanceInKm);
		newTrip.setCustomer(customer);
		newTrip.setDriver(driverAvailable);
		newTrip.setFromLocation(fromLocation);
		newTrip.setToLocation(toLocation);
		newTrip.setStatus(TripStatus.CONFIRMED);

		customer.getTripBookingList().add(newTrip);

		driverAvailable.getTripBookingList().add(newTrip);

		customerRepository2.save(customer);

		driverRepository2.save(driverAvailable);

		return newTrip;
	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
		Customer customer = tripBooking.getCustomer();
		Driver driver = tripBooking.getDriver();
		customer.getTripBookingList().remove(tripBooking);
		driver.getTripBookingList().remove(tripBooking);

		tripBooking.setStatus(TripStatus.CANCELED);

		customer.getTripBookingList().add(tripBooking);
		driver.getTripBookingList().add(tripBooking);

		customerRepository2.save(customer);
		driverRepository2.save(driver);
	}

	@Override
	public void completeTrip(Integer tripId) {
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
		tripBooking.setStatus(TripStatus.COMPLETED);
		Customer customer = tripBooking.getCustomer();
		Driver driver = tripBooking.getDriver();

		customerRepository2.save(customer);
		driverRepository2.save(driver);
	}
}
