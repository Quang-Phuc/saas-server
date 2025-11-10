package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.Customer;
import com.phuclq.student.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RestEntityResponse restEntityRes;

    // ✅ Tạo mới khách hàng
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.createCustomer(customer));
    }

    // ✅ Cập nhật khách hàng
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customer));
    }

    // ✅ Xoá khách hàng
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Lấy danh sách khách hàng
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    // ✅ Lấy chi tiết khách hàng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Customer>> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    // ✅ API tìm kiếm theo phoneNumber hoặc identityNumber
    @GetMapping("/search")
    public ResponseEntity<?> findCustomer(
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String identityNumber) {

        Optional<Customer> customer = customerService.findByPhoneOrIdentity(phoneNumber, identityNumber);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(customer).getResponse();

    }
}
