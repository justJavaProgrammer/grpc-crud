package testing.faker;

import com.odeyalo.grpc.books.api.grpc.Book.UpdateBookRequest;

import java.util.UUID;

public final class UpdateBookRequestFaker {
    UpdateBookRequest.Builder builder = UpdateBookRequest.newBuilder();

    public UpdateBookRequestFaker(String bookId) {
        UpdateBookRequest.UpdateBookPayload updateBookPayload = UpdateBookPayloadFaker.create().get();

        builder.setBookId(bookId)
                .setNewBook(updateBookPayload);
    }

    public static UpdateBookRequestFaker withId(UUID id) {
        return new UpdateBookRequestFaker(id.toString());
    }

    public UpdateBookRequest get() {
        return builder.build();
    }
}
