package com.phuclq.student.service.impl;

import com.phuclq.student.domain.Customer;
import com.phuclq.student.repository.CustomerRepository;
import com.phuclq.student.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Long id, Customer customer) {
        return customerRepository.findById(id).map(existing -> {
            existing.setFullName(customer.getFullName());
            existing.setPhoneNumber(customer.getPhoneNumber());
            existing.setDateOfBirth(customer.getDateOfBirth());
            existing.setIdentityNumber(customer.getIdentityNumber());
            existing.setIssueDate(customer.getIssueDate());
            existing.setIssuePlace(customer.getIssuePlace());
            existing.setPermanentAddress(customer.getPermanentAddress());
            existing.setGender(customer.getGender());
            existing.setEmail(customer.getEmail());
            return customerRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Customer not found with id " + id));
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Optional<Customer> findByPhoneOrIdentity(String phoneNumber, String identityNumber) {
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            return customerRepository.findByPhoneNumber(phoneNumber);
        } else if (identityNumber != null && !identityNumber.isEmpty()) {
            return customerRepository.findByIdentityNumber(identityNumber);
        } else {
            return Optional.empty();
        }
    }
}
