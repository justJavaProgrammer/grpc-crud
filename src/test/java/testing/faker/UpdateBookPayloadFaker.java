package testing.faker;

import com.github.javafaker.Faker;
import com.odeyalo.grpc.books.api.grpc.Book.UpdateBookRequest.UpdateBookPayload;

public final class UpdateBookPayloadFaker {
    final UpdateBookPayload.Builder builder = UpdateBookPayload.newBuilder();
    final Faker faker = Faker.instance();

    public UpdateBookPayloadFaker() {
        builder
                .setQuantity(faker.random().nextInt(0, 100))
                .setAuthor(faker.book().author())
                .setName(faker.book().title())
                .setIsbn(faker.code().isbn10());
    }

    public static UpdateBookPayloadFaker create() {
        return new UpdateBookPayloadFaker();
    }

    public UpdateBookPayload get() {
        return builder.build();
    }

    public UpdateBookPayloadFaker setTitle(String title) {
        builder.setName(title);
        return this;
    }

    public UpdateBookPayloadFaker setIsbn(String isbn) {
        builder.setIsbn(isbn);
        return this;
    }

    public UpdateBookPayloadFaker setAuthorName(String authorName) {
        builder.setAuthor(authorName);
        return this;
    }
}
