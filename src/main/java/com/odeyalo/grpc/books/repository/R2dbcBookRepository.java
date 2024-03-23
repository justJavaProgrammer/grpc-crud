package com.odeyalo.grpc.books.repository;

import com.odeyalo.grpc.books.entity.BookEntity;
import com.odeyalo.grpc.books.repository.r2dbc.delegate.R2dbcBookRepositoryDelegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public final class R2dbcBookRepository implements BookRepository {
    private final R2dbcBookRepositoryDelegate delegate;

    public R2dbcBookRepository(R2dbcBookRepositoryDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    @NotNull
    public Mono<BookEntity> findBookById(@Nullable UUID id) {
        if ( id == null ) {
            return Mono.empty();
        }
        return delegate.findById(id);
    }

    @Override
    @NotNull
    public Mono<BookEntity> save(@NotNull BookEntity entity) {
        return delegate.save(entity);
    }

    @Override
    @NotNull
    public Mono<Void> removeById(@Nullable UUID id) {
        if ( id == null ) {
            return Mono.empty();
        }
        return delegate.deleteById(id);
    }
}
