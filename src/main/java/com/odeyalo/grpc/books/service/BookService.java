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
                .map(it -> newBookValues.asBook(bookId))
                // redundant converting, maybe UpdateBookInfo should provide method 'asBookEntity' similar to 'asBook(UUID)'?
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

    private BookEntity createBookEntity(CreateBookInfo book) {
        return BookEntity.builder()
                .name(book.getName())
                .author(book.getAuthor())
                .quantity(book.getQuantity())
                .isbn(book.getIsbn())
                .build();
    }
}
