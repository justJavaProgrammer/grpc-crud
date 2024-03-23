package com.odeyalo.grpc.books.support.converter;

import com.odeyalo.grpc.books.client.book.Book.BookDto;
import com.odeyalo.grpc.books.entity.BookEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookDtoConverter {

    BookDto toBookDto(BookEntity entity);

}