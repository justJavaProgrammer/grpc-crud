package com.odeyalo.grpc.books.support.converter;

import com.odeyalo.grpc.books.api.grpc.Book.CreateBookRequest;
import com.odeyalo.grpc.books.service.CreateBookInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreateBookInfoConverter {

    CreateBookInfo toCreateBookInfo(CreateBookRequest request);

}
