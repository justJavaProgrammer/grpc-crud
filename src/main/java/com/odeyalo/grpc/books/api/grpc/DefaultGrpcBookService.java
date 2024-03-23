package com.odeyalo.grpc.books.api.grpc;

import com.odeyalo.grpc.books.client.book.Book.BookDto;
import com.odeyalo.grpc.books.client.book.Book.CreateBookRequest;
import com.odeyalo.grpc.books.client.book.Book.FetchBookRequest;
import com.odeyalo.grpc.books.client.book.BookServiceGrpc;
import com.odeyalo.grpc.books.exception.BookNotFoundException;
import com.odeyalo.grpc.books.model.Book;
import com.odeyalo.grpc.books.service.BookService;
import com.odeyalo.grpc.books.support.converter.BookDtoConverter;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@GrpcService
public final class DefaultGrpcBookService extends BookServiceGrpc.BookServiceImplBase {
    private final BookService bookService;
    private final BookDtoConverter bookDtoConverter;

    public DefaultGrpcBookService(BookService bookService, BookDtoConverter bookDtoConverter) {
        this.bookService = bookService;
        this.bookDtoConverter = bookDtoConverter;
    }

    @Override
    public void fetchBook(@NotNull final FetchBookRequest request,
                          @NotNull final StreamObserver<BookDto> responseObserver) {

        doFetchBook(request)
                .map(bookDtoConverter::toBookDto)
                .switchIfEmpty(Mono.defer(() -> Mono.error(BookNotFoundException.defaultException())))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);

    }

    @Override
    public void addBook(CreateBookRequest request, StreamObserver<BookDto> responseObserver) {


        responseObserver.onNext(BookDto.newBuilder()
                        .setId("123")
                        .setName("unknown")
                        .setAuthor("123")
                        .setIsbn("12")
                        .setQuantity(1)
                .build());
        responseObserver.onCompleted();
    }

    @NotNull
    private Mono<Book> doFetchBook(@NotNull FetchBookRequest request) {
        return bookService.findBookById(UUID.fromString(request.getBookId()));
    }
}
