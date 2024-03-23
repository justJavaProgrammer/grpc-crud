package com.odeyalo.grpc.books.api.grpc;

import com.odeyalo.grpc.books.api.grpc.Book.BookDto;
import com.odeyalo.grpc.books.api.grpc.Book.CreateBookRequest;
import com.odeyalo.grpc.books.api.grpc.Book.FetchBookRequest;
import com.odeyalo.grpc.books.api.grpc.Book.UpdateBookRequest;
import com.odeyalo.grpc.books.exception.BookNotFoundException;
import com.odeyalo.grpc.books.model.Book;
import com.odeyalo.grpc.books.service.BookService;
import com.odeyalo.grpc.books.service.CreateBookInfo;
import com.odeyalo.grpc.books.service.UpdateBookInfo;
import com.odeyalo.grpc.books.support.converter.BookDtoConverter;
import com.odeyalo.grpc.books.support.converter.CreateBookInfoConverter;
import com.odeyalo.grpc.books.support.converter.UpdateBookInfoConverter;
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
    private final CreateBookInfoConverter createBookInfoConverter;
    private final UpdateBookInfoConverter updateBookInfoConverter;

    public DefaultGrpcBookService(BookService bookService,
                                  BookDtoConverter bookDtoConverter,
                                  CreateBookInfoConverter createBookInfoConverter,
                                  UpdateBookInfoConverter updateBookInfoConverter) {
        this.bookService = bookService;
        this.bookDtoConverter = bookDtoConverter;
        this.createBookInfoConverter = createBookInfoConverter;
        this.updateBookInfoConverter = updateBookInfoConverter;
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
