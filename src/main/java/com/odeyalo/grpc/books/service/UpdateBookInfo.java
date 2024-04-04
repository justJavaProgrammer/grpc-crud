package com.odeyalo.grpc.books.service;

import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.net.URI;

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
    @NotNull
    URI coverImage;
}
