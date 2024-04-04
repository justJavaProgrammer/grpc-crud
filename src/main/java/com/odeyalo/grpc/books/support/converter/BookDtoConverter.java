package com.odeyalo.grpc.books.support.converter;

import com.odeyalo.grpc.books.api.grpc.Book.BookDto;
import com.odeyalo.grpc.books.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookDtoConverter {

    @Mapping(target = "coverImage", expression = "java(book.getCoverImage().toString())")
    BookDto toBookDto(Book book);

}
