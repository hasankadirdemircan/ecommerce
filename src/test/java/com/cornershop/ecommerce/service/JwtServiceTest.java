package com.cornershop.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cornershop.ecommerce.config.UserInfoDetails;
import com.cornershop.ecommerce.dto.LoginDto;
import com.cornershop.ecommerce.helper.CustomerDoFactory;
import com.cornershop.ecommerce.model.Customer;
import com.cornershop.ecommerce.repository.CustomerRepository;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private Authentication authentication;

    private CustomerDoFactory customerDoFactory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.customerDoFactory = new CustomerDoFactory();
    }

    @Test
    void validateToken_expiredTrue() {
        UserInfoDetails userInfoDetails = Optional.of(customerDoFactory.getCustomer()).map(UserInfoDetails::new)                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        Customer customer = customerDoFactory.getCustomer();

        when(customerRepository.findByEmail(any())).thenReturn(Optional.of(customer));

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        Collection<SimpleGrantedAuthority> authCollection = Collections.singleton(simpleGrantedAuthority);
        when(authentication.getAuthorities())
                .thenReturn((Collection) authCollection);
        when(authentication.getName()).thenReturn(customer.getEmail());

        LoginDto response = jwtService.generateToken(authentication);

        boolean isValidate = jwtService.validateToken(response.getToken(), userInfoDetails);

        assertEquals(true, isValidate);
    }

}
