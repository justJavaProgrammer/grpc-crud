package com.odeyalo.grpc.books.api.grpc;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import reactor.test.StepVerifier;

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
}
