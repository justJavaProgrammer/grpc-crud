package com.odeyalo.grpc.books.repository;

import com.odeyalo.grpc.books.entity.BookEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryBookRepository implements BookRepository {
    private final Map<UUID, BookEntity> store = new ConcurrentHashMap<>();

    public InMemoryBookRepository(BookEntity... books) {
        Arrays.stream(books).forEach(
                it -> store.put(it.getId(), it)
        );
    }

    public static InMemoryBookRepository withBooks(BookEntity... books) {
        return new InMemoryBookRepository(books);
    }

    public static InMemoryBookRepository empty() {
        return new InMemoryBookRepository();
    }

    @Override
    @NotNull
    public Mono<BookEntity> findBookById(@Nullable UUID id) {

        return Mono.justOrEmpty(
                store.get(id)
        );
    }

    @Override
    @NotNull
    public Mono<BookEntity> save(@NotNull BookEntity entity) {
        return null;
    }

    @Override
    @NotNull
    public Mono<Void> removeById(@Nullable UUID id) {
        return null;
    }
}
