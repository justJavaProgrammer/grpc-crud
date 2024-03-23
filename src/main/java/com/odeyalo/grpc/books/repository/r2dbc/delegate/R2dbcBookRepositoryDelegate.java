package com.odeyalo.grpc.books.repository.r2dbc.delegate;

import com.odeyalo.grpc.books.entity.BookEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Delegate that executes sql statements using R2DBC.
 * Used by {@link com.odeyalo.grpc.books.repository.R2dbcBookRepository}
 */
@Repository
public interface R2dbcBookRepositoryDelegate extends R2dbcRepository<BookEntity, UUID> {
}
