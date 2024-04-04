package testing.faker;

import com.github.javafaker.Faker;
import com.odeyalo.grpc.books.api.grpc.Book;

public class CreateBookRequestFaker {
    final Book.CreateBookRequest.Builder builder = Book.CreateBookRequest.newBuilder();
    final Faker faker = Faker.instance();

    public CreateBookRequestFaker() {
        builder
                .setAuthor(faker.book().author())
                .setIsbn(faker.code().isbn10())
                .setName(faker.book().title())
                .setQuantity(faker.random().nextInt(0, 50))
                .setImageUrl(faker.avatar().image());
    }


    public static CreateBookRequestFaker create() {
        return new CreateBookRequestFaker();
    }

    public Book.CreateBookRequest get() {
        return builder.build();
    }

    public CreateBookRequestFaker setTitle(String title) {
        builder.setName(title);
        return this;
    }

    public CreateBookRequestFaker setIsbn(String isbn) {
        builder.setIsbn(isbn);
        return this;
    }

    public CreateBookRequestFaker setAuthorName(String authorName) {
        builder.setAuthor(authorName);
        return this;
    }

    public CreateBookRequestFaker setQuantity(int quantity) {
        builder.setQuantity(quantity);
        return this;
    }

    public CreateBookRequestFaker setCoverImage(String imageUrl) {
        builder.setImageUrl(imageUrl);
        return this;
    }
}
