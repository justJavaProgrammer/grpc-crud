package com.odeyalo.grpc.books.api.grpc;

import com.odeyalo.grpc.books.exception.RequestValidationException;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import reactor.test.StepVerifier;
import testing.faker.CreateBookRequestFaker;

import static testing.factory.DefaultGrpcReactiveBookServiceTestableBuilder.testableBuilder;

class AddBookEndpointTest {
    public static final Book.CreateBookRequest CREATE_NOVEL_REQUEST = Book.CreateBookRequest.newBuilder()
            .setName("Three Days of Happiness")
            .setIsbn("12345677899")
            .setAuthor("Sugaru Miaki")
            .setQuantity(10)
            .setImageUrl("https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1591954433i/53972440.jpg")
            .build();

    @Test
    void shouldReturnSavedBookWithGeneratedId() {
        DefaultReactiveBookService testable = testableBuilder().build();
        // when
        testable.addBook(CREATE_NOVEL_REQUEST)
                .as(StepVerifier::create)
                // then
                .expectNextMatches(it -> StringUtils.isNotBlank(it.getId()))
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorIfBookNameIsLessThan1Symbol() {
        // given
        DefaultReactiveBookService testable = testableBuilder().build();

        Book.CreateBookRequest bookRequest = CreateBookRequestFaker.create()
                .setTitle("")
                .get();
        // when
        testable.addBook(bookRequest)
                .as(StepVerifier::create)
                // then
                .expectError(RequestValidationException.class)
                .verify();
    }

    @Test
    void shouldReturnErrorIfBookCoverImageIsNotValidURI() {
        // given
        DefaultReactiveBookService testable = testableBuilder().build();
        Book.CreateBookRequest createBookRequest = CreateBookRequestFaker.create()
                .setCoverImage("invalid")
                .get();
        // when
        testable.addBook(createBookRequest)
                .as(StepVerifier::create)
                .expectError(RequestValidationException.class)
                .verify();
    }

    @Test
    void shouldReturnErrorIfBookIsbnIsLessThan10Symbols() {
        // given
        DefaultReactiveBookService testable = testableBuilder().build();

        Book.CreateBookRequest bookRequest = CreateBookRequestFaker.create()
                .setIsbn("less")
                .get();
        // when
        testable.addBook(bookRequest)
                .as(StepVerifier::create)
                // then
                .expectError(RequestValidationException.class)
                .verify();
    }

    @Test
    void shouldReturnErrorIfBookIsbnIsMoreThan15Symbols() {
        // given
        DefaultReactiveBookService testable = testableBuilder().build();

        Book.CreateBookRequest bookRequest = CreateBookRequestFaker.create()
                .setIsbn("greater_than_15_symbols_string")
                .get();
        // when
        testable.addBook(bookRequest)
                .as(StepVerifier::create)
                // then
                .expectError(RequestValidationException.class)
                .verify();
    }

    @Test
    void shouldReturnErrorIfBookAuthorNameIsLessThan5Symbols() {
        // given
        DefaultReactiveBookService testable = testableBuilder().build();

        Book.CreateBookRequest bookRequest = CreateBookRequestFaker.create()
                .setAuthorName("less")
                .get();
        // when
        testable.addBook(bookRequest)
                .as(StepVerifier::create)
                // then
                .expectError(RequestValidationException.class)
                .verify();
    }

    @Test
    void shouldReturnErrorIfBookQuantityIsLessThan0() {
        // given
        DefaultReactiveBookService testable = testableBuilder().build();

        Book.CreateBookRequest bookRequest = CreateBookRequestFaker.create()
                .setQuantity(-1)
                .get();
        // when
        testable.addBook(bookRequest)
                .as(StepVerifier::create)
                // then
                .expectError(RequestValidationException.class)
                .verify();
    }

    @Test
    void shouldCompleteWithoutErrorIfZeroQuantityIsUsed() {
        // given
        DefaultReactiveBookService testable = testableBuilder().build();

        Book.CreateBookRequest bookRequest = CreateBookRequestFaker.create()
                .setQuantity(0)
                .get();
        // when
        testable.addBook(bookRequest)
                .as(StepVerifier::create)
                // then
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void shouldReturnSavedBookWithTitleAsProvided() {
        DefaultReactiveBookService testable = testableBuilder().build();
        // when
        testable.addBook(CREATE_NOVEL_REQUEST)
                .map(Book.BookDto::getName)
                .as(StepVerifier::create)
                // then
                .expectNext(CREATE_NOVEL_REQUEST.getName())
                .verifyComplete();
    }

    @Test
    void shouldReturnSavedBookWithIsbnAsProvided() {
        DefaultReactiveBookService testable = testableBuilder().build();
        // when
        testable.addBook(CREATE_NOVEL_REQUEST)
                .map(Book.BookDto::getIsbn)
                .as(StepVerifier::create)
                // then
                .expectNext(CREATE_NOVEL_REQUEST.getIsbn())
                .verifyComplete();
    }

    @Test
    void shouldReturnSavedBookWithAuthorAsProvided() {
        DefaultReactiveBookService testable = testableBuilder().build();
        // when
        testable.addBook(CREATE_NOVEL_REQUEST)
                .map(Book.BookDto::getAuthor)
                .as(StepVerifier::create)
                // then
                .expectNext(CREATE_NOVEL_REQUEST.getAuthor())
                .verifyComplete();
    }

    @Test
    void shouldReturnSavedBookWithQuantityAsProvided() {
        DefaultReactiveBookService testable = testableBuilder().build();
        // when
        testable.addBook(CREATE_NOVEL_REQUEST)
                .map(Book.BookDto::getQuantity)
                .as(StepVerifier::create)
                // then
                .expectNext(CREATE_NOVEL_REQUEST.getQuantity())
                .verifyComplete();
    }

    @Test
    void bookShouldBeFoundAfterSave() {
        DefaultReactiveBookService testable = testableBuilder().build();
        // when
        Book.BookDto savedBook = testable.addBook(CREATE_NOVEL_REQUEST).block();

        //noinspection DataFlowIssue
        Book.FetchBookRequest fetchExistingBooKRequest = Book.FetchBookRequest.newBuilder()
                .setBookId(savedBook.getId())
                .build();
        // then
        testable.fetchBook(fetchExistingBooKRequest)
                .as(StepVerifier::create)
                .expectNext(savedBook)
                .verifyComplete();
    }
}
