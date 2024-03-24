package com.odeyalo.grpc.books.config;

import build.buf.protovalidate.Validator;
import com.odeyalo.grpc.books.support.validation.ReactiveGrpcRequestValidator;
import com.odeyalo.grpc.books.support.validation.WrapperReactiveGrpcRequestValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestValidationConfiguration {

    @Bean
    public ReactiveGrpcRequestValidator requestValidator() {
        return new WrapperReactiveGrpcRequestValidator(new Validator());
    }
}
