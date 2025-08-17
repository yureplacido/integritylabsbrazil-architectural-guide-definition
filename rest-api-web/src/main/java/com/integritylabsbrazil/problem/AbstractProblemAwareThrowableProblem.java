package com.integritylabsbrazil.problem;


import static com.integritylabsbrazil.tools.json.JsonUtil.asJsonNode;
import static com.integritylabsbrazil.tools.json.JsonUtil.readValue;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import java.util.Map;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.StatusType;

public abstract class AbstractProblemAwareThrowableProblem extends AbstractThrowableProblem {

    public AbstractProblemAwareThrowableProblem(ProblemAware ex, StatusType status, URI location) {
        super(ex.getType(), titleOf(ex, status), status, detailsOf(ex), location, null, parametersOf(ex));
    }

    public AbstractProblemAwareThrowableProblem(ProblemAware ex, StatusType status) {
        this(ex, status, null);
    }

    private static String titleOf(ProblemAware ex, StatusType status) {
        return (ex.getTitle() == null) ? status.getReasonPhrase() : ex.getTitle();
    }

    private static String detailsOf(ProblemAware ex) {
        return ex.getDetail();
    }

    private static Map<String, Object> parametersOf(ProblemAware ex) {
        return parametersOf(ex.getParameters());
    }

    private static Map<String, Object> parametersOf(Object parameters) {
        if (parameters == null) {
            return null;
        } else if (parameters instanceof Map) {
            return (Map<String, Object>) parameters;
        } else if (parameters instanceof JsonNode node) {
            return readValue(node, Map.class);
        } else {
            return readValue(asJsonNode(parameters), Map.class);
        }
    }
}
