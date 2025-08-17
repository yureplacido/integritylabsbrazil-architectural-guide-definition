package com.integritylabsbrazil.exception;

import java.net.URI;

public class InsufficientBalanceException extends ProblemAwareExeption {

    private static final URI TYPE = URI.create("https://www.integritylabsbrazil.com/problem/insufficient-balance");

    public InsufficientBalanceException() {
        super("The balance is insufficient to complete the transaction.");
    }

    @Override
    public URI getType() {
        return TYPE;
    }

    @Override
    public String getTitle() {
        return "Insufficient Balance";
    }

    @Override
    public String getDetail() {
        return "The balance is insufficient to complete the transaction.";
    }

}
