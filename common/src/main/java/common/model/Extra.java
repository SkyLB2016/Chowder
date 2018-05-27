package common.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SKY on 2017/6/15.
 * 默认的bundle传输类
 */
public class Extra<T> implements Serializable {
    String text;
    String text2;
    T entity;
    List<T> entities;

    public Extra() {
    }

    public Extra(String text) {
        this.text = text;
    }

    public Extra(String extra1, String extra2) {
        this.text = extra1;
        this.text2 = extra2;
    }

    public Extra(T extra3) {
        this.entity = extra3;
    }

    public Extra(List<T> extra4) {
        this.entities = extra4;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public List<T> getEntities() {
        return entities;
    }

    public void setEntities(List<T> entities) {
        this.entities = entities;
    }
}
