package com.websocket.java.util.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by anton
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {

    private Long id;
    private String from;
    private String message;
    private TreeSet<User> to;
    // content
    private String contentData;
    private String contentDataType;
    // system
    private Set<Long> ridden;
    private Set<String> riddenBy;
    private Boolean isInDialog;
    private String arrivedTime;
    private Set<Long> deleted;

}
