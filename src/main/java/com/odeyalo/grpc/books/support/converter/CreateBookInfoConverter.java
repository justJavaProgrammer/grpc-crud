package com.odeyalo.grpc.books.support.converter;

import com.odeyalo.grpc.books.client.book.Book;
import com.odeyalo.grpc.books.service.CreateBookInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreateBookInfoConverter {

    CreateBookInfo toCreateBookInfo(Book.CreateBookRequest request);

}
