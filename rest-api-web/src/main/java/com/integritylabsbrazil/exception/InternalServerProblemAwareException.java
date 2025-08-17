package com.integritylabsbrazil.exception;

import com.integritylabsbrazil.problem.ProblemAware;
import java.net.URI;
import java.util.Map;
import java.util.UUID;

public class InternalServerProblemAwareException extends Throwable implements ProblemAware<Map<String, Object>> {

    private static final URI TYPE = URI.create("https://www.integritylabsbrazil.com/problem/internal-server-error");

    private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

    private static final String INTERNAL_SERVER_ERROR_DETAIL = "An unexpected/unrecoverable error has occurred";

    private final UUID uuid;

    public InternalServerProblemAwareException(Throwable e, UUID uuid) {
        super("%s : %s".formatted(INTERNAL_SERVER_ERROR_DETAIL, uuid), e);
        this.uuid = uuid;
    }

    @Override
    public String getDetail() {
        return INTERNAL_SERVER_ERROR_DETAIL;
    }

    @Override
    public String getTitle() {
        return INTERNAL_SERVER_ERROR;
    }

    @Override
    public URI getType() {
        return TYPE;
    }

    @Override
    public Map<String, Object> getParameters() {
        return Map.of("error-context", this.uuid.toString());
    }

}
