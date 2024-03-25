package testing.factory;

import build.buf.protovalidate.Validator;
import com.odeyalo.grpc.books.api.grpc.DefaultReactiveBookService;
import com.odeyalo.grpc.books.entity.BookEntity;
import com.odeyalo.grpc.books.repository.BookRepository;
import com.odeyalo.grpc.books.repository.InMemoryBookRepository;
import com.odeyalo.grpc.books.service.BookService;
import com.odeyalo.grpc.books.support.converter.BookConverterImpl;
import com.odeyalo.grpc.books.support.converter.BookDtoConverterImpl;
import com.odeyalo.grpc.books.support.validation.ReactiveGrpcRequestValidator;
import com.odeyalo.grpc.books.support.validation.WrapperReactiveGrpcRequestValidator;

public class DefaultGrpcReactiveBookServiceTestableBuilder {
    private BookRepository bookRepository = new InMemoryBookRepository();
    private final ReactiveGrpcRequestValidator requestValidator = new WrapperReactiveGrpcRequestValidator(new Validator());

    public static DefaultGrpcReactiveBookServiceTestableBuilder testableBuilder() {
        return new DefaultGrpcReactiveBookServiceTestableBuilder();
    }

    public DefaultGrpcReactiveBookServiceTestableBuilder withBooks(BookEntity... books) {
        bookRepository = new InMemoryBookRepository(books);
        return this;
    }

    public DefaultReactiveBookService build() {
        return new DefaultReactiveBookService(
                requestValidator,
                new BookService(bookRepository, new BookConverterImpl()),
                new BookDtoConverterImpl());
    }
}
