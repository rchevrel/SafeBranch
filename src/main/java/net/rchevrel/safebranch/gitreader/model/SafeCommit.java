package net.rchevrel.safebranch.gitreader.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SafeCommit {

    private final String id;

    private final String message;

    private final String shortMessage;

    private final String messageFirstLine;

    private final List<SafeCommit> parents;

    public SafeCommit(String id, String message, String shortMessage, String messageFirstLine) {
        this.id = id;
        this.message = message;
        this.shortMessage = shortMessage;
        this.messageFirstLine = messageFirstLine;
        this.parents = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getShortMessage() {
        return shortMessage;
    }

    public String getMessageFirstLine() {
        return messageFirstLine;
    }

    public List<SafeCommit> getParents() {
        return parents;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SafeCommit safeCommit = (SafeCommit) o;
        return Objects.equals(id, safeCommit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Commit{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", shortMessage='" + shortMessage + '\'' +
                ", messageFirstLine='" + messageFirstLine + '\'' +
                ", parents=" + parents.size() +
                '}';
    }
}
