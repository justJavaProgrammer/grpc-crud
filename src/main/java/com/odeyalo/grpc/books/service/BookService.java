package com.odeyalo.grpc.books.service;

import com.odeyalo.grpc.books.entity.BookEntity;
import com.odeyalo.grpc.books.exception.BookUpdateFailedException;
import com.odeyalo.grpc.books.support.converter.BookConverter;
import com.odeyalo.grpc.books.model.Book;
import com.odeyalo.grpc.books.repository.BookRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
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
    public Mono<Book> save(@NotNull CreateBookInfo book) {
        BookEntity bookEntity = createBookEntity(book);

        return bookRepository.save(bookEntity)
                .map(bookConverter::toBook);
    }

    @NotNull
    public Mono<Book> updateBook(@NotNull UUID bookId,
                                 @NotNull UpdateBookInfo newBookValues) {

        return bookRepository.findBookById(bookId)
                .map(it -> withUpdatedValues(newBookValues, it))
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

    private static BookEntity withUpdatedValues(@NotNull UpdateBookInfo newBookValues, BookEntity it) {
        return BookEntity.builder()
                .id(it.getId())
                .quantity(newBookValues.getQuantity())
                .author(newBookValues.getAuthor())
                .name(newBookValues.getName())
                .isbn(newBookValues.getIsbn())
                .build();
    }

    private static BookEntity createBookEntity(CreateBookInfo book) {
        return BookEntity.builder()
                .name(book.getName())
                .author(book.getAuthor())
                .quantity(book.getQuantity())
                .isbn(book.getIsbn())
                .build();
    }
}
