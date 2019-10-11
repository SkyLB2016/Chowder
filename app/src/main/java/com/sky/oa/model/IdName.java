package com.sky.oa.model;

import java.io.Serializable;

public class IdName implements Serializable {
    public String id;
    public String name;

    public IdName() {
    }

    public IdName(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
