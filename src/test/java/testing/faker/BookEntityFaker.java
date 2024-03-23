package testing.faker;

import com.odeyalo.grpc.books.entity.BookEntity;

import java.util.UUID;

public final class BookEntityFaker {
    private final BookEntity.BookEntityBuilder builder = BookEntity.builder();

    public BookEntityFaker() {
        builder
                .id(UUID.randomUUID());
    }

    public static BookEntityFaker create() {
        return new BookEntityFaker();
    }

    public BookEntity get() {
        return builder.build();
    }
}
