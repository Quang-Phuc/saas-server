package com.phuclq.student.service;

import com.phuclq.student.domain.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    Customer createCustomer(Customer customer);

    Customer updateCustomer(Integer id, Customer customer);

    void deleteCustomer(Integer id);

    List<Customer> getAllCustomers();

    Optional<Customer> getCustomerById(Integer id);

    Optional<Customer> findByPhoneOrIdentity(String phoneNumber, String identityNumber);
}
