package cz.meind.microomega.User;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Objects;

public class Exchange implements Serializable {
    private User one;
    private User two;

    private LinkedList<Hook> messages;

    public Exchange(User one, User two) {
        this.one = one;
        this.two = two;
        messages = new LinkedList<>();
    }

    public User getOne() {
        return one;
    }

    public User getTwo() {
        return two;
    }

    public LinkedList<Hook> getMessages() {
        return messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exchange exchange = (Exchange) o;
        return Objects.equals(one, exchange.one) && Objects.equals(two, exchange.two) && Objects.equals(messages, exchange.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(one, two, messages);
    }
}
