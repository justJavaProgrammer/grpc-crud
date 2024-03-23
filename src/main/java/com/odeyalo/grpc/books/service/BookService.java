package com.odeyalo.grpc.books.service;

import com.odeyalo.grpc.books.entity.BookEntity;
import com.odeyalo.grpc.books.exception.BookUpdateFailedException;
import com.odeyalo.grpc.books.support.converter.BookConverter;
import com.odeyalo.grpc.books.model.Book;
import com.odeyalo.grpc.books.repository.BookRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.util.UUID;

public final class BookService {
    private final BookRepository bookRepository;
    private final BookConverter bookConverter;

    public BookService(BookRepository bookRepository, BookConverter bookConverter) {
        this.bookRepository = bookRepository;
        this.bookConverter = bookConverter;
    }

    @NotNull
    public Mono<Book> findBookById(@Nullable UUID id) {
        return bookRepository.findBookById(id)
                .map(bookConverter::toBook);
    }

    @NotNull
    public Mono<Book> save(@NotNull Book book) {
        BookEntity bookEntity = bookConverter.toBookEntity(book);

        return bookRepository.save(bookEntity)
                .map(bookConverter::toBook);
    }

    @NotNull
    public Mono<Book> updateBook(@NotNull UUID bookId,
                                 @NotNull UpdateBookInfo newBookValues) {

        return bookRepository.findBookById(bookId)
                .map(it -> newBookValues.asBook(bookId))
                .map(bookConverter::toBookEntity)
                .flatMap(bookRepository::save)
                .map(bookConverter::toBook)
                .switchIfEmpty(Mono.defer(
                        () -> Mono.error(BookUpdateFailedException::defaultException)
                ));
    }

    @NotNull
    public Mono<Void> removeById(@Nullable UUID id) {
        return bookRepository.removeById(id);
    }

}
