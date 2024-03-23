package com.odeyalo.grpc.books.api.grpc;

import com.odeyalo.grpc.books.api.grpc.Book.BookDto;
import com.odeyalo.grpc.books.api.grpc.Book.CreateBookRequest;
import com.odeyalo.grpc.books.api.grpc.Book.FetchBookRequest;
import com.odeyalo.grpc.books.api.grpc.Book.UpdateBookRequest;
import com.odeyalo.grpc.books.exception.BookNotFoundException;
import com.odeyalo.grpc.books.model.Book;
import com.odeyalo.grpc.books.service.BookService;
import com.odeyalo.grpc.books.service.CreateBookInfo;
import com.odeyalo.grpc.books.support.converter.BookDtoConverter;
import com.odeyalo.grpc.books.support.converter.CreateBookInfoConverter;
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

    public DefaultGrpcBookService(BookService bookService,
                                  BookDtoConverter bookDtoConverter,
                                  CreateBookInfoConverter createBookInfoConverter) {
        this.bookService = bookService;
        this.bookDtoConverter = bookDtoConverter;
        this.createBookInfoConverter = createBookInfoConverter;
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

        BookDto bookDto = BookDto.newBuilder()
                .setId(request.getBookId())
                .setAuthor(request.getNewBook().getAuthor())
                .setIsbn(request.getNewBook().getIsbn())
                .setName(request.getNewBook().getName())
                .setQuantity(request.getNewBook().getQuantity())
                .build();

        responseObserver.onNext(bookDto);
        responseObserver.onCompleted();
    }

    @NotNull
    private Mono<Book> doFetchBook(@NotNull FetchBookRequest request) {
        return bookService.findBookById(UUID.fromString(request.getBookId()));
    }
}
