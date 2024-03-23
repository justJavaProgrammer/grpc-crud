package com.odeyalo.grpc.books.api.grpc;

import com.odeyalo.grpc.books.client.book.Book;
import com.odeyalo.grpc.books.client.book.Book.BookDto;
import com.odeyalo.grpc.books.client.book.BookServiceGrpc;
import com.odeyalo.grpc.books.entity.BookEntity;
import com.odeyalo.grpc.books.repository.BookRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.jetbrains.annotations.NotNull;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@GrpcService
public final class DefaultGrpcBookService extends BookServiceGrpc.BookServiceImplBase {
    private final BookRepository bookRepository;

    public DefaultGrpcBookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void fetchBook(@NotNull final Book.FetchBookRequest request,
                          @NotNull final StreamObserver<BookDto> responseObserver) {

        bookRepository.findBookById(UUID.fromString(request.getBookId()))
                .map(this::toBookDto)
                .subscribeOn(Schedulers.parallel())
                .subscribe(responseObserver::onNext,
                        responseObserver::onError,
                        responseObserver::onCompleted);

    }

    private BookDto toBookDto(BookEntity entity) {
        return BookDto.newBuilder()
                .setId(entity.getId().toString())
                .setName(entity.getName())
                .build();
    }
}
