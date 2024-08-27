package com.loan_calculator.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loan_calculator.api.domain.customer.Customer;
import com.loan_calculator.api.domain.customer.CustomerRequestDTO;
import com.loan_calculator.api.domain.customer.CustomerResponseDTO;
import com.loan_calculator.api.repositories.CustomerRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository repository;

    public Customer create(CustomerRequestDTO data) {
        Customer newCustomer = new Customer();
        newCustomer.setName(data.name());
        newCustomer.setCpf(data.cpf());

        this.repository.save(newCustomer);

        return newCustomer;
    }

    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = this.repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Customer not found")); 
        return new CustomerResponseDTO(customer.getId(), customer.getName(), customer.getCpf());
    }

    public List<CustomerResponseDTO> getCustomerList() {
        List<Customer> customers = this.repository.findAll();
        return customers.stream().map(c -> new CustomerResponseDTO(c.getId(), c.getName(), c.getCpf())).collect(Collectors.toList());
    }

    public String deleteCustomerById(Long id) {
        this.repository.deleteById(id);
        return "Cliente deletado com sucesso!";
    }

    public Customer updateCustomerById(Long id, CustomerRequestDTO customerDetails) {
        Customer customer = this.repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        
        Customer updatedCustomer = new Customer(
                customer.getId(),
                customerDetails.cpf() != null ? customerDetails.cpf() : customer.getCpf(),
                customerDetails.name() != null ? customerDetails.name() : customer.getName()
        );

        return this.repository.save(updatedCustomer);
    }
}
