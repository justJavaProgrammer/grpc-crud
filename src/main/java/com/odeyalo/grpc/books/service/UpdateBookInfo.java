package com.odeyalo.grpc.books.service;

import com.odeyalo.grpc.books.model.Book;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Value
@Builder
public class UpdateBookInfo {
    @NotNull
    String name;
    @NotNull
    String author;
    @NotNull
    String isbn;
    int quantity;

    public Book asBook(UUID id) {
        // Maybe it is not a good place to do it here.
        // But if not here, then where? Don't want to create a new converter
        return Book.builder()
                .id(id)
                .author(this.getAuthor())
                .name(this.getName())
                .isbn(this.getIsbn())
                .quantity(this.getQuantity())
                .build();
    }
}
