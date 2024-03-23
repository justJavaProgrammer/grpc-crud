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
                .name(faker.book().title());
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
}
