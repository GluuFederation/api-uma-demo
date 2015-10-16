package com.uma.server.common;

import java.io.Serializable;

public class Resource implements Serializable {

    private String id;

    public Resource() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
