package com.odeyalo.grpc.books.support.converter;

import com.odeyalo.grpc.books.api.grpc.Book.UpdateBookRequest.UpdateBookPayload;
import com.odeyalo.grpc.books.service.UpdateBookInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UpdateBookInfoConverter {

    UpdateBookInfo toUpdateBookInfo(UpdateBookPayload payload);

}
