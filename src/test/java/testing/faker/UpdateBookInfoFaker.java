package testing.faker;

import com.github.javafaker.Faker;
import com.odeyalo.grpc.books.service.UpdateBookInfo;

import java.net.URI;

public final class UpdateBookInfoFaker {
    private final UpdateBookInfo.UpdateBookInfoBuilder builder = UpdateBookInfo.builder();
    private final Faker faker = Faker.instance();

    public UpdateBookInfoFaker() {
        builder
                .quantity(faker.random().nextInt(0, 100))
                .author(faker.book().author())
                .name(faker.book().title())
                .isbn(faker.code().isbn10())
                .coverImage(URI.create(faker.avatar().image()));
    }

    public static UpdateBookInfoFaker create() {
        return new UpdateBookInfoFaker();
    }

    public UpdateBookInfo get() {
        return builder.build();
    }
}
