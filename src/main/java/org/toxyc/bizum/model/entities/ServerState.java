package org.toxyc.bizum.model.entities;

public enum ServerState {
    SUCCESS(0),
    INVALID_USERNAME(1),
    INVALID_EMAIL(2),
    INVALID_PASSWORD(3),
    DATABASE_ERROR(4);

    public Integer state;

    ServerState(Integer state) {
        this.state = state;
    }

    public Integer toInt() {
        return this.state;
    }
}