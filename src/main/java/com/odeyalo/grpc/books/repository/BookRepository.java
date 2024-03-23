package com.odeyalo.grpc.books.repository;

import com.odeyalo.grpc.books.entity.BookEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface BookRepository {

    @NotNull
    Mono<BookEntity> findBookById(@Nullable UUID id);

    @NotNull
    Mono<BookEntity> save(@NotNull BookEntity entity);

    @NotNull
    Mono<Void> removeById(@Nullable UUID id);
}
