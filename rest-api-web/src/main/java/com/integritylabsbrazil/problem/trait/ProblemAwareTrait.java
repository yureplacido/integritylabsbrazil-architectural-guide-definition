package com.integritylabsbrazil.problem.trait;

import com.integritylabsbrazil.exception.ProblemAwareExeption;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.Problem;
import org.zalando.problem.spring.common.AdviceTrait;

public interface ProblemAwareTrait extends AdviceTrait {

    @ExceptionHandler(ProblemAwareExeption.class)
    default ResponseEntity<Problem> handleProblemAware(ProblemAwareExeption ex, HttpServletRequest request) {

        Problem problem = Problem.builder()
                                 .withType(ex.getType() != null ? ex.getType() : URI.create("about:blank"))
                                 .withTitle(ex.getTitle() != null ? ex.getTitle() : "An error occurred")
                                 .withDetail(ex.getDetail() != null ? ex.getDetail() : "An unexpected error occurred")
                                 .with("parameters", ex.getParameters())
                                 .with("timestamp", Instant.now().toString())
                                 .with("path", request.getRequestURI())
                                 .withStatus(ex.getStatus())
                                 .build();
        return ResponseEntity
            .status(HttpStatus.valueOf(ex.getStatus().getStatusCode()))
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(problem);
    }
}
