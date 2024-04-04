package com.odeyalo.grpc.books.model;

import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.UUID;

@Value
@Builder
public class Book {
    @NotNull
    UUID id;
    @NotNull
    String name;
    @NotNull
    String author;
    @NotNull
    String isbn;
    int quantity;
    @NotNull
    URI coverImage;
}
