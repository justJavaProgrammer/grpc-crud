package testing.faker;

import com.github.javafaker.Faker;
import com.odeyalo.grpc.books.model.Book;

import java.net.URI;
import java.util.UUID;

public final class BookFaker {
    private final Book.BookBuilder builder = Book.builder();
    private final Faker faker = Faker.instance();

    public BookFaker() {
        builder
                .id(UUID.randomUUID())
                .quantity(faker.random().nextInt(0, 100))
                .author(faker.book().author())
                .name(faker.book().title())
                .isbn(faker.code().isbn10())
                .coverImage(URI.create(faker.avatar().image()));
    }

    public static BookFaker create() {
        return new BookFaker();
    }

    public Book get() {
        return builder.build();
    }

    public BookFaker setId(UUID id) {
        builder.id(id);
        return this;
    }
}
