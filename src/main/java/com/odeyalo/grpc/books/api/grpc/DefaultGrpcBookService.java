package com.odeyalo.grpc.books.api.grpc;

import com.odeyalo.grpc.books.client.book.Book.BookDto;
import com.odeyalo.grpc.books.client.book.Book.FetchBookRequest;
import com.odeyalo.grpc.books.client.book.BookServiceGrpc;
import com.odeyalo.grpc.books.entity.BookEntity;
import com.odeyalo.grpc.books.repository.BookRepository;
import com.odeyalo.grpc.books.support.converter.BookDtoConverter;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@GrpcService
public final class DefaultGrpcBookService extends BookServiceGrpc.BookServiceImplBase {
    private final BookRepository bookRepository;
    private final BookDtoConverter bookDtoConverter;

    public DefaultGrpcBookService(BookRepository bookRepository, BookDtoConverter bookDtoConverter) {
        this.bookRepository = bookRepository;
        this.bookDtoConverter = bookDtoConverter;
    }

    @Override
    public void fetchBook(@NotNull final FetchBookRequest request,
                          @NotNull final StreamObserver<BookDto> responseObserver) {

        doFetchBook(request)
                .map(bookDtoConverter::toBookDto)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);

    }

    @NotNull
    private Mono<BookEntity> doFetchBook(@NotNull FetchBookRequest request) {
        return bookRepository.findBookById(UUID.fromString(request.getBookId()));
    }
}
