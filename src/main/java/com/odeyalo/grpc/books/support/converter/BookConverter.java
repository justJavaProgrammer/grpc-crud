package com.odeyalo.grpc.books.support.converter;

import com.odeyalo.grpc.books.entity.BookEntity;
import com.odeyalo.grpc.books.model.Book;
import org.mapstruct.Mapper;

/**
 * Converts from {@link Book} to {@link BookEntity} and vice-versa
 */
@Mapper(componentModel = "spring")
public interface BookConverter {

    Book toBook(BookEntity entity);

    BookEntity toBookEntity(Book book);

}
