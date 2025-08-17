package com.integritylabsbrazil.controller;

import com.integritylabsbrazil.exception.ExceptionWithParammeter;
import com.integritylabsbrazil.exception.InsufficientBalanceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FooController {

    @GetMapping("/runtime-exception")
    public ResponseEntity<String> testException() {
        throw new RuntimeException("Erro proposital para teste do ControllerAdvice");
    }

    @GetMapping("/insufficient-balance")
    public ResponseEntity<String> insufficientBalance() throws Throwable {
        throw new InsufficientBalanceException();
    }

    @GetMapping("/exception-with-parameter")
    public ResponseEntity<String> exceptionWithParammeter() throws Throwable {
        throw new ExceptionWithParammeter();
    }
}
