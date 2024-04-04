package com.odeyalo.grpc.books.support.converter;

import com.odeyalo.grpc.books.api.grpc.Book.UpdateBookRequest.UpdateBookPayload;
import com.odeyalo.grpc.books.service.UpdateBookInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UpdateBookInfoConverter {

    @Mapping(target = "coverImage", expression = "java(java.net.URI.create(payload.getImageUrl()))")
    UpdateBookInfo toUpdateBookInfo(UpdateBookPayload payload);

}
