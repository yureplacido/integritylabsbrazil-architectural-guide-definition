package com.integritylabsbrazil.exception;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class ExceptionWithParammeter extends ProblemAwareExeption {

    private static final URI TYPE = URI.create("https://www.integritylabsbrazil.com/problem/exception-with-parameter");

    public ExceptionWithParammeter() {
        super("An error occurred with a parameter.");
    }

    @Override
    public URI getType() {
        return TYPE;
    }

    @Override
    public String getTitle() {
        return "Parameter Error";
    }

    @Override
    public String getDetail() {
        return "An error occurred with a parameter.";
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> maps = new HashMap<>();
        maps.put("foo", "abc");
        maps.put("bar", "cba");
        return Map.of("p1", "v1", "p2", maps);
    }
}
