package com.odeyalo.grpc.books.support.converter;

import com.odeyalo.grpc.books.api.grpc.Book.CreateBookRequest;
import com.odeyalo.grpc.books.service.CreateBookInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreateBookInfoConverter {

    @Mapping(target = "coverImage", expression = "java(java.net.URI.create(request.getImageUrl()))")
    CreateBookInfo toCreateBookInfo(CreateBookRequest request);

}
