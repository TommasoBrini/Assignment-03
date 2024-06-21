package org.simulation.actors;

import java.util.Optional;

public record Message<T, X>(String name, T content, Optional<X> content2) {
    public Message(String name, T content) {
        this(name, content, Optional.empty());
    }

    public Message(String name, T content, X content2) {
        this(name, content, Optional.ofNullable(content2));
    }
}
