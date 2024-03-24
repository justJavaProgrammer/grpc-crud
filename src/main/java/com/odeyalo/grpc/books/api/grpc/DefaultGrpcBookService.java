package com.odeyalo.grpc.books.api.grpc;

import build.buf.protovalidate.Validator;
import com.odeyalo.grpc.books.api.grpc.Book.*;
import com.odeyalo.grpc.books.exception.BookNotFoundException;
import com.odeyalo.grpc.books.exception.RequestValidationException;
import com.odeyalo.grpc.books.model.Book;
import com.odeyalo.grpc.books.service.BookService;
import com.odeyalo.grpc.books.service.CreateBookInfo;
import com.odeyalo.grpc.books.service.UpdateBookInfo;
import com.odeyalo.grpc.books.support.converter.BookDtoConverter;
import com.odeyalo.grpc.books.support.converter.CreateBookInfoConverter;
import com.odeyalo.grpc.books.support.converter.UpdateBookInfoConverter;
import com.odeyalo.grpc.books.support.validation.ReactiveGrpcRequestValidator;
import com.odeyalo.grpc.books.support.validation.WrapperReactiveGrpcRequestValidator;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@GrpcService()
public final class DefaultGrpcBookService extends BookServiceGrpc.BookServiceImplBase {
    private final BookService bookService;
    private final ReactiveGrpcRequestValidator grpcRequestValidator;
    private final BookDtoConverter bookDtoConverter;
    private final CreateBookInfoConverter createBookInfoConverter;

    public DefaultGrpcBookService(BookService bookService,
                                  ReactiveGrpcRequestValidator grpcRequestValidator, BookDtoConverter bookDtoConverter,
                                  CreateBookInfoConverter createBookInfoConverter,
                                  UpdateBookInfoConverter updateBookInfoConverter) {
        this.bookService = bookService;
        this.grpcRequestValidator = grpcRequestValidator;
        this.bookDtoConverter = bookDtoConverter;
        this.createBookInfoConverter = createBookInfoConverter;
        this.updateBookInfoConverter = updateBookInfoConverter;
    }
    private final UpdateBookInfoConverter updateBookInfoConverter;

    @Override
    public void fetchBook(@NotNull final FetchBookRequest request,
                          @NotNull final StreamObserver<BookDto> responseObserver) {

        validate(request)
                .flatMap(it -> doFetchBook(request)
                        .map(bookDtoConverter::toBookDto)
                        .switchIfEmpty(Mono.defer(() -> Mono.error(BookNotFoundException.defaultException()))))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);

    }

    @NotNull
    private Mono<@NotNull FetchBookRequest> validate(@NotNull FetchBookRequest request) {

        return grpcRequestValidator.validate(request)
                .flatMap(it -> {
                    if ( it.isSuccess() ) {
                        return Mono.just(request);
                    }
                    return Mono.error(RequestValidationException.defaultException());
                });
    }

    @Override
    public void addBook(CreateBookRequest request, StreamObserver<BookDto> responseObserver) {

        CreateBookInfo createBookInfo = createBookInfoConverter.toCreateBookInfo(request);

        bookService.save(createBookInfo)
                .map(bookDtoConverter::toBookDto)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);
    }

    @Override
    public void updateBook(UpdateBookRequest request, StreamObserver<BookDto> responseObserver) {
        UpdateBookInfo updateBookInfo = toUpdateBookInfo(request);

        bookService.updateBook(UUID.fromString(request.getBookId()), updateBookInfo)
                .map(bookDtoConverter::toBookDto)
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorMap(it -> BookNotFoundException.defaultException())
                .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);
    }

    @Override
    public void removeBook(DeleteBookRequest request, StreamObserver<DeleteBookResponse> responseObserver) {

        bookService.removeById(UUID.fromString(request.getBookId()))
                .thenReturn(
                        DeleteBookResponse.newBuilder()
                                .setStatus(DeletionStatus.SUCCESS)
                                .build()
                )
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);
    }

    private UpdateBookInfo toUpdateBookInfo(UpdateBookRequest request) {
        return updateBookInfoConverter.toUpdateBookInfo(request.getNewBook());
    }

    @NotNull
    private Mono<Book> doFetchBook(@NotNull FetchBookRequest request) {
        return bookService.findBookById(UUID.fromString(request.getBookId()));
    }
}
