package com.phuclq.student.service;

import com.phuclq.student.domain.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    Customer createCustomer(Customer customer);

    Customer updateCustomer(Long id, Customer customer);

    void deleteCustomer(Long id);

    List<Customer> getAllCustomers();

    Optional<Customer> getCustomerById(Long id);

    Optional<Customer> findByPhoneOrIdentity(String phoneNumber, String identityNumber);
}
