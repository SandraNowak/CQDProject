package com.cqdTaskProcessing.CQD.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Builder
@Setter
@Getter
public class Task {

    private UUID id;
    private Status status;
    private Result result;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id.toString(), task.id.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id.toString());
    }
}
