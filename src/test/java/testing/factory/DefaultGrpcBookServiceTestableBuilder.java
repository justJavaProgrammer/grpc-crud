package testing.factory;

import build.buf.protovalidate.Validator;
import com.odeyalo.grpc.books.api.grpc.DefaultGrpcBookService;
import com.odeyalo.grpc.books.entity.BookEntity;
import com.odeyalo.grpc.books.repository.BookRepository;
import com.odeyalo.grpc.books.repository.InMemoryBookRepository;
import com.odeyalo.grpc.books.service.BookService;
import com.odeyalo.grpc.books.support.converter.BookConverterImpl;
import com.odeyalo.grpc.books.support.converter.BookDtoConverterImpl;
import com.odeyalo.grpc.books.support.converter.CreateBookInfoConverterImpl;
import com.odeyalo.grpc.books.support.converter.UpdateBookInfoConverterImpl;
import com.odeyalo.grpc.books.support.validation.ReactiveGrpcRequestValidator;
import com.odeyalo.grpc.books.support.validation.WrapperReactiveGrpcRequestValidator;

public class DefaultGrpcBookServiceTestableBuilder {
    private BookRepository bookRepository = new InMemoryBookRepository();
    private final ReactiveGrpcRequestValidator requestValidator = new WrapperReactiveGrpcRequestValidator(new Validator());

    public static DefaultGrpcBookServiceTestableBuilder testableBuilder() {
        return new DefaultGrpcBookServiceTestableBuilder();
    }

    public DefaultGrpcBookServiceTestableBuilder withBooks(BookEntity... books) {
        bookRepository = new InMemoryBookRepository(books);
        return this;
    }

    public DefaultGrpcBookService build() {
        return new DefaultGrpcBookService(
                new BookService(bookRepository, new BookConverterImpl()),
                requestValidator, new BookDtoConverterImpl(),
                new CreateBookInfoConverterImpl(),
                new UpdateBookInfoConverterImpl());
    }
}
