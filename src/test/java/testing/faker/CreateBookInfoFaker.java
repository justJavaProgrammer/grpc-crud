package testing.faker;

import com.github.javafaker.Faker;
import com.odeyalo.grpc.books.service.CreateBookInfo;

public final class CreateBookInfoFaker {
    CreateBookInfo.CreateBookInfoBuilder builder = CreateBookInfo.builder();
    Faker faker = Faker.instance();

    public CreateBookInfoFaker() {
        builder
                .quantity(faker.random().nextInt(0, 100))
                .author(faker.book().author())
                .name(faker.book().title())
                .isbn(faker.code().isbn10());
    }

    public static CreateBookInfoFaker create() {
        return new CreateBookInfoFaker();
    }

    public CreateBookInfo get() {
        return builder.build();
    }
}
