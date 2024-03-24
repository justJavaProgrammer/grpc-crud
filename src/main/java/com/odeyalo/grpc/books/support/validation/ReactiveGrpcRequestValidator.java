package com.odeyalo.grpc.books.support.validation;

import build.buf.protovalidate.ValidationResult;
import com.google.protobuf.Message;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface ReactiveGrpcRequestValidator {

    @NotNull
    Mono<ValidationResult> validate(@NotNull Message message);
}
