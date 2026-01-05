package com.crni99.bookstore.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.crni99.bookstore.model.Book;
import com.crni99.bookstore.service.BillingService;
import com.crni99.bookstore.service.EmailService;
import com.crni99.bookstore.service.ShoppingCartService;

@WebMvcTest(CheckoutController.class)
class CheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BillingService billingService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private ShoppingCartService shoppingCartService;

    @Test
    void shouldPlaceOrder() throws Exception {

        // Arrange
        when(shoppingCartService.getCart())
                .thenReturn(List.of(new Book()));

        // Act + Assert
        mockMvc.perform(post("/checkout/placeOrder")
                .with(csrf())
                .param("email", "mail@example.com")
                .param("firstName", "John")
                .param("lastName", "Doe"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/cart"));

        // Verify
        verify(emailService).sendEmail(
                eq("mail@example.com"),
                eq("bookstore - Order Confirmation"),
                eq("Your order has been confirmed.")
        );
    }
}
