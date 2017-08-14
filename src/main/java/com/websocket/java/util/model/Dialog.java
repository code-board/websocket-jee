package com.websocket.java.util.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;
import java.util.Set;

/**
 * Created by anton
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Dialog {

    private Set<User> dialog;
    private List<Message> messages;
    private Long seq;
}
