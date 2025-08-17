package com.integritylabsbrazil.problem;

import com.integritylabsbrazil.exception.InternalServerProblemAwareException;
import lombok.extern.slf4j.Slf4j;
import org.zalando.problem.Status;

@Slf4j
public class InternalServerErrorProblem extends AbstractProblemAwareThrowableProblem {

    public InternalServerErrorProblem(final InternalServerProblemAwareException exception) {
        super(exception, Status.INTERNAL_SERVER_ERROR);
        log.error("Internal Server Error: {}", exception.getMessage(), exception);
    }
}