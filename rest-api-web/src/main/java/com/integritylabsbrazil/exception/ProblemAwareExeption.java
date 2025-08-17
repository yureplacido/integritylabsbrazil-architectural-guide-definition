package com.integritylabsbrazil.exception;

import com.integritylabsbrazil.problem.ProblemAware;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract class for exceptions that are aware of problems. It implements the ProblemAware interface with a Map<String, Object> as the parameter type. This class extends RuntimeException and logs the error message when an instance is created.
 */
@Slf4j
public abstract class ProblemAwareExeption extends RuntimeException implements ProblemAware<Map<String, Object>> {

    public ProblemAwareExeption(String message) {
        super(message);
        log.error(message);
    }

    public ProblemAwareExeption(Throwable e) {
        super(e);
        log.error(e.getMessage());
    }
}
