package com.integritylabsbrazil.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.jackson.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

@Slf4j
@Configuration
public class ObjectMapperConfig implements InitializingBean {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> builder
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .featuresToDisable(
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                SerializationFeature.FAIL_ON_EMPTY_BEANS,
                DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE
                              )
            .modules(
                new JavaTimeModule(),
                new ProblemModule(),
                new ConstraintViolationProblemModule()
                    );
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("ObjectMapper configuration initialized with modules [ProblemModule, ConstraintViolationProblemModule, JavaTimeModule]");
    }
}
