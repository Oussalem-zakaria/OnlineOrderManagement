package com.fortest.myorders.customer.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.fortest.myorders.customer.bean.Customer;
import com.fortest.myorders.customer.repository.CustomerRepository;
import com.fortest.myorders.customer.request.CustomerRequest;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer createCustomer(CustomerRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .build();

        return customerRepository.saveAndFlush(customer);
        // todo: check if email valid
        // todo: check if email not taken

    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Integer id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Customer updateCustomer(Integer id, CustomerRequest request) {
        return customerRepository.findById(id).map(customer -> {
            customer.setFirstName(request.getFirstName());
            customer.setLastName(request.getLastName());
            customer.setEmail(request.getEmail());
            return customerRepository.save(customer);
        }).orElseGet(() -> {
            Customer customer = Customer.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .build();
            return customerRepository.save(customer);
        });
    }

    public void deleteCustomer(Integer id) {
        customerRepository.deleteById(id);
    }
}
