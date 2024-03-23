package com.odeyalo.grpc.books.service;

import com.odeyalo.grpc.books.entity.BookEntity;
import com.odeyalo.grpc.books.model.Book;
import com.odeyalo.grpc.books.repository.BookRepository;
import com.odeyalo.grpc.books.repository.InMemoryBookRepository;
import com.odeyalo.grpc.books.support.converter.BookConverter;
import com.odeyalo.grpc.books.support.converter.BookConverterImpl;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import testing.faker.BookEntityFaker;
import testing.faker.BookFaker;
import testing.faker.UpdateBookInfoFaker;

import java.util.UUID;

class BookServiceTest {

    @Test
    void shouldReturnExistingBookById() {
        BookEntity book = BookEntityFaker.create().get();

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
        BookEntity book = BookEntityFaker.create().get();
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

    static final class TestableBuilder {
        private BookRepository bookRepository = new InMemoryBookRepository();
        private BookConverter bookConverter = new BookConverterImpl();

        public static TestableBuilder builder() {
            return new TestableBuilder();
        }

        public TestableBuilder withBooks(BookEntity entity) {
            bookRepository = InMemoryBookRepository.withBooks(entity);
            return this;
        }

        public BookService build() {
            return new BookService(bookRepository, bookConverter);
        }
    }

}