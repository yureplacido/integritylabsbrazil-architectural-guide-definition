package com.integritylabsbrazil.problem;

import java.net.URI;
import org.zalando.problem.Status;

public interface ProblemAware<ParameterType> {

    default URI getType() { return null; }

    default String getDetail() {
        return null;
    }

    default String getTitle() {
        return null;
    }

    default Status getStatus() { return Status.BAD_REQUEST; }

    default ParameterType getParameters() {
        return null;
    }
}
