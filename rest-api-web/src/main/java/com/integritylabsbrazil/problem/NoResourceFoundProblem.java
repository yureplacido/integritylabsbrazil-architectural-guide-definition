package com.integritylabsbrazil.problem;


import java.net.URI;
import java.util.HashMap;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;


public class NoResourceFoundProblem extends AbstractThrowableProblem {

    private static final URI TYPE = URI.create("https://www.integritylabsbrazil.com/problem/no-resource-found");

    public NoResourceFoundProblem(NoResourceFoundException cause, HashMap<String, Object> parameters) {
        super(TYPE, "Resource Not Found", Status.NOT_FOUND, cause.getMessage(), null, null, parameters);
    }
}
