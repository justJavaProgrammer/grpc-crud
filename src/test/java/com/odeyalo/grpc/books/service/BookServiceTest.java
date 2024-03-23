package com.odeyalo.grpc.books.service;

import com.odeyalo.grpc.books.exception.BookUpdateFailedException;
import com.odeyalo.grpc.books.model.Book;
import com.odeyalo.grpc.books.repository.BookRepository;
import com.odeyalo.grpc.books.repository.InMemoryBookRepository;
import com.odeyalo.grpc.books.support.converter.BookConverter;
import com.odeyalo.grpc.books.support.converter.BookConverterImpl;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import testing.faker.BookFaker;
import testing.faker.UpdateBookInfoFaker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

class BookServiceTest {

    @Test
    void shouldReturnExistingBookById() {
        Book book = BookFaker.create().get();

        var testable = TestableBuilder
                .builder()
                .withBooks(book)
                .build();

        testable.findBookById(book.getId())
                .map(Book::getId)
                .as(StepVerifier::create)
                .expectNext(book.getId())
                .verifyComplete();
    }

    @Test
    void shouldReturnNothingIfBookDoesNotExist() {
        var testable = TestableBuilder.builder().build();

        testable.findBookById(UUID.randomUUID())
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldSaveBook() {
        // given
        Book book = BookFaker.create().get();

        var testable = TestableBuilder.builder().build();
        // when
        testable.save(book)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
        // then
        testable.findBookById(book.getId())
                .as(StepVerifier::create)
                .expectNext(book)
                .verifyComplete();
    }

    @Test
    void shouldUpdateBookWithNewAuthorName() {
        // given
        Book book = BookFaker.create().get();
        UpdateBookInfo newBook = UpdateBookInfoFaker.create().get();

        var testable = TestableBuilder.builder()
                .withBooks(book)
                .build();
        // when
        testable.updateBook(book.getId(), newBook)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        // then
        testable.findBookById(book.getId())
                .map(Book::getAuthor)
                .as(StepVerifier::create)
                .expectNext(newBook.getAuthor())
                .verifyComplete();
    }

    @Test
    void shouldThrowExceptionIfUpdateIsRequestedButBookDoesNotExist() {
        UpdateBookInfo newBook = UpdateBookInfoFaker.create().get();

        var testable = TestableBuilder.builder().build();
        // when
        testable.updateBook(UUID.randomUUID(), newBook)
                .as(StepVerifier::create)
                .expectError(BookUpdateFailedException.class)
                .verify();
    }

    @Test
    void shouldRemoveExistingBook() {
        Book book = BookFaker.create().get();

        var testable = TestableBuilder
                .builder()
                .withBooks(book)
                .build();
        // when
        testable.removeById(book.getId())
                .as(StepVerifier::create)
                .verifyComplete();

        // then expect nothing to be found
        testable.findBookById(book.getId())
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldNotRemoveAnyBookIfDoesNotExist() {
        Book book = BookFaker.create().get();

        var testable = TestableBuilder
                .builder()
                .withBooks(book)
                .build();
        // when
        testable.removeById(UUID.randomUUID())
                .as(StepVerifier::create)
                .verifyComplete();

        // then expect nothing to be found
        testable.findBookById(book.getId())
                .as(StepVerifier::create)
                .expectNext(book)
                .verifyComplete();
    }

    static final class TestableBuilder {
        private final BookRepository bookRepository = new InMemoryBookRepository();
        private final BookConverter bookConverter = new BookConverterImpl();
        private final List<Book> toSave = new ArrayList<>();

        public static TestableBuilder builder() {
            return new TestableBuilder();
        }

        public TestableBuilder withBooks(Book... books) {
            toSave.addAll(Arrays.asList(books));
            return this;
        }

        public BookService build() {
            BookService bookService = new BookService(bookRepository, bookConverter);

            for (Book book : toSave) {
                bookService.save(book).block();
            }

            return bookService;
        }
    }

}