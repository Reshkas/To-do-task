package todo.model;

import java.util.stream.Stream;

public enum Status {
    CREATED,
    INPROGRESS,
    DONE;

    public static Status of(String status) {
        return Stream.of(Status.values())
                .filter(s -> s.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cannot find Status by " + status));
    }
}
