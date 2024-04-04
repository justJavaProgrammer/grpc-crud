package com.odeyalo.grpc.books.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.net.URI;

@Value
@Builder
@AllArgsConstructor(staticName = "of")
public class CreateBookInfo {
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
