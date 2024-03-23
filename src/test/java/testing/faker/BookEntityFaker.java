package testing.faker;

import com.github.javafaker.Faker;
import com.odeyalo.grpc.books.entity.BookEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class BookEntityFaker {
    private final BookEntity.BookEntityBuilder builder = BookEntity.builder();
    private final Faker faker = Faker.instance();

    public BookEntityFaker() {
        builder
                .id(UUID.randomUUID())
                .name(faker.book().title())
                .author(faker.book().author())
                .isbn(faker.code().isbn10())
                .quantity(faker.random().nextInt(0, 50));
    }

    public static BookEntityFaker create() {
        return new BookEntityFaker();
    }

    public BookEntity get() {
        return builder.build();
    }

    public BookEntityFaker setId(@Nullable UUID id) {
        builder.id(id);
        return this;
    }

    public BookEntityFaker setTitle(@NotNull String title) {
        builder.name(title);
        return this;
    }

    public BookEntityFaker setAuthor(@NotNull String author) {
        builder.author(author);
        return this;
    }

    public BookEntityFaker setIsbn(@NotNull String isbn) {
        builder.isbn(isbn);
        return this;
    }

    public BookEntityFaker setQuantity(@NotNull int quantity) {
        builder.quantity(quantity);
        return this;
    }
}
