package com.odeyalo.grpc.books.entity;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@Table("books")
public class BookEntity implements Persistable<UUID> {
    @Id
    UUID id;
    @NotNull
    @Column("title")
    String name;
    @NotNull
    String author;
    @NotNull
    String isbn;
    int quantity;

    @Override
    public boolean isNew() {
        return id == null;
    }
}
