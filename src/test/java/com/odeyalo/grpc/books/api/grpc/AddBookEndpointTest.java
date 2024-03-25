package com.odeyalo.grpc.books.api.grpc;

import com.odeyalo.grpc.books.exception.RequestValidationException;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import reactor.test.StepVerifier;
import testing.faker.CreateBookRequestFaker;

import static testing.factory.DefaultGrpcReactiveBookServiceTestableBuilder.testableBuilder;

class AddBookEndpointTest extends AbstractBookClientTest {
    public static final Book.CreateBookRequest CREATE_NOVEL_REQUEST = Book.CreateBookRequest.newBuilder()
            .setName("Three Days of Happiness")
            .setIsbn("12345677899")
            .setAuthor("Sugaru Miaki")
            .setQuantity(10)
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
}
