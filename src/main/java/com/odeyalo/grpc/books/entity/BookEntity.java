package com.odeyalo.grpc.books.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.util.UUID;

@Data
@Builder
public class BookEntity {
    UUID id;
}
