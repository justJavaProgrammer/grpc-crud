package com.odeyalo.grpc.books.support.converter;

import com.odeyalo.grpc.books.api.grpc.Book.BookDto;
import com.odeyalo.grpc.books.entity.BookEntity;
import com.odeyalo.grpc.books.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookDtoConverter {

    BookDto toBookDto(BookEntity entity);

    BookDto toBookDto(Book book);

}
