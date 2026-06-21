package com.gdb.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdb.account.dto.request.SavingsAccountRequest;
import com.gdb.account.dto.response.AccountResponse;
import com.gdb.account.exception.AccountException;
import com.gdb.account.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    // GET /api/v1/accounts/{accountNumber} — 200
    @Test
    public void testGetAccountByNumber_Success() throws Exception {
        AccountResponse mockResponse = AccountResponse.builder()
                .accountNumber(1001L)
                .name("John Doe")
                .balance(BigDecimal.valueOf(5000.00))
                .isActive(true)
                .build();

        when(accountService.getAccountByNumber(1001L)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/accounts/1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(1001))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.balance").value(5000.00));
    }

    // GET /api/v1/accounts/{accountNumber} — 404
    @Test
    public void testGetAccountByNumber_NotFound() throws Exception {
        when(accountService.getAccountByNumber(9999L))
                .thenThrow(new AccountException("Account not found with number: 9999", "ACCOUNT_NOT_FOUND"));

        mockMvc.perform(get("/api/v1/accounts/9999"))
                .andExpect(status().isNotFound());
    }

    // POST /api/v1/accounts/savings — 201
    @Test
    public void testCreateSavingsAccount_Success() throws Exception {
        SavingsAccountRequest request = new SavingsAccountRequest();
        request.setName("Jane Doe");
        request.setAadharNumber("123456789012");
        request.setDateOfBirth("1990-01-01");
        request.setPin("1234");
        request.setGender("Female");
        request.setPhoneNo("9876543210");
        request.setPrivilege("SILVER");
        request.setInitialBalance(BigDecimal.valueOf(2000));

        // rest unchanged
    }

    // POST /api/v1/accounts/savings — 400
    @Test
    public void testCreateSavingsAccount_BadRequest() throws Exception {
        // unchanged — empty {} still works
    }

    // POST /api/v1/accounts/savings — 422
    @Test
    public void testCreateSavingsAccount_ValidationFailure() throws Exception {
        SavingsAccountRequest request = new SavingsAccountRequest();
        request.setName("Jane Doe");
        request.setAadharNumber("000000000000");
        request.setDateOfBirth("1990-01-01");
        request.setPin("1234");
        request.setGender("Female");
        request.setPhoneNo("9876543210");
        request.setPrivilege("SILVER");
        request.setInitialBalance(BigDecimal.valueOf(2000));

        // rest unchanged
    }
}