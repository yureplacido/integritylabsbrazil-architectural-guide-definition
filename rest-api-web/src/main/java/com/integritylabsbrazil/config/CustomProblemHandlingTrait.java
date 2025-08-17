package com.integritylabsbrazil.config;

import com.integritylabsbrazil.problem.NoResourceFoundProblem;
import com.integritylabsbrazil.problem.trait.InternalServerErrorTrait;
import com.integritylabsbrazil.problem.trait.ProblemAwareTrait;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.zalando.problem.Problem;

public interface CustomProblemHandlingTrait extends InternalServerErrorTrait, ProblemAwareTrait {

    @ExceptionHandler(NoResourceFoundException.class)
    default ResponseEntity<Problem> handleNoResourceFound(NoResourceFoundException ex, NativeWebRequest request) {

        HashMap<String, Object> params = new HashMap<>();

        params.put("params",
                   Map.of("timestamp", Instant.now().toString(),
                          "path", request.getNativeRequest(HttpServletRequest.class).getRequestURI()));
        NoResourceFoundProblem problem = new NoResourceFoundProblem(ex, params);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                             .body(problem);
    }
}
