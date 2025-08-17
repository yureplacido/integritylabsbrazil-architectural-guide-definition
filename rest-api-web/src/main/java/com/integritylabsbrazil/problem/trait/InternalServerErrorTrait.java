package com.integritylabsbrazil.problem.trait;

import com.integritylabsbrazil.exception.InternalServerProblemAwareException;
import com.integritylabsbrazil.problem.InternalServerErrorProblem;
import java.util.UUID;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.zalando.problem.Problem;
import org.zalando.problem.spring.web.advice.general.ThrowableAdviceTrait;

public interface InternalServerErrorTrait extends ThrowableAdviceTrait {

    /**
     * Handles any Throwable by wrapping it in an InternalServerErrorProblem.
     *
     * @param throwable the Throwable to handle
     * @param request   the NativeWebRequest
     * @return a ResponseEntity containing the InternalServerErrorProblem
     */
    @Override
    default ResponseEntity<Problem> handleThrowable(Throwable throwable, NativeWebRequest request) {

        if (throwable instanceof ResponseStatusException) {
            throw (ResponseStatusException) throwable;
        }

        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(throwable.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            HttpStatus status = responseStatus.value();
            throw new ResponseStatusException(status, throwable.getMessage(), throwable);
        }

        UUID uuid = UUID.randomUUID();
        InternalServerErrorProblem problem = new InternalServerErrorProblem(new InternalServerProblemAwareException(throwable, uuid));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                             .body(problem);
    }
}
