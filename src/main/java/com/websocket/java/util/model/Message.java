package com.websocket.java.util.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by anton
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {

    private long id;
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
    private Timestamp arrivedTime;
    private Set<Long> deleted;

}
