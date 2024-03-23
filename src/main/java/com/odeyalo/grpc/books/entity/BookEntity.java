package com.odeyalo.grpc.books.entity;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Data
@Builder
public class BookEntity {
    UUID id;
    @NotNull
    String name;
    @NotNull
    String author;
    @NotNull
    String isbn;
    int quantity;
}
