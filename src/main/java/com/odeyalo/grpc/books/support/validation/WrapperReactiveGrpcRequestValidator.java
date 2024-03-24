package com.odeyalo.grpc.books.support.validation;

import build.buf.protovalidate.ValidationResult;
import build.buf.protovalidate.Validator;
import com.google.protobuf.Message;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public final class WrapperReactiveGrpcRequestValidator implements ReactiveGrpcRequestValidator {
    private final Validator validator;

    public WrapperReactiveGrpcRequestValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    @NotNull
    public Mono<ValidationResult> validate(@NotNull Message message) {
        return Mono.fromCallable(() -> validator.validate(message));
    }
}
