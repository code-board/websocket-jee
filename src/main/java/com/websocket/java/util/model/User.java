package com.websocket.java.util.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/**
 * Created by anton
 */
@Getter
@Setter
@EqualsAndHashCode(exclude = {"isInDialog"})
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class User implements Comparable<User> {

    private String name;
    private Boolean isInDialog;

    public User(String from) {
        this.name = from;
    }

    @Override
    public int compareTo(User user) {
        if (this.equals(user))
            return 0;
        return this.name.compareTo(user.getName());
    }
}
