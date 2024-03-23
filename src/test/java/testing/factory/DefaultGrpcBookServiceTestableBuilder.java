package testing.factory;

import com.odeyalo.grpc.books.api.grpc.DefaultGrpcBookService;
import com.odeyalo.grpc.books.entity.BookEntity;
import com.odeyalo.grpc.books.repository.BookRepository;
import com.odeyalo.grpc.books.repository.InMemoryBookRepository;
import com.odeyalo.grpc.books.service.BookService;
import com.odeyalo.grpc.books.support.converter.BookConverterImpl;
import com.odeyalo.grpc.books.support.converter.BookDtoConverterImpl;
import com.odeyalo.grpc.books.support.converter.CreateBookInfoConverterImpl;
import com.odeyalo.grpc.books.support.converter.UpdateBookInfoConverterImpl;

public class DefaultGrpcBookServiceTestableBuilder {
    private BookRepository bookRepository = new InMemoryBookRepository();

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
                new BookDtoConverterImpl(),
                new CreateBookInfoConverterImpl(),
                new UpdateBookInfoConverterImpl());
    }
}
