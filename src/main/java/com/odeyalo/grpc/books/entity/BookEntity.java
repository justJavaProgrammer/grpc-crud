package com.odeyalo.grpc.books.entity;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class BookEntity {
    UUID id;
}
