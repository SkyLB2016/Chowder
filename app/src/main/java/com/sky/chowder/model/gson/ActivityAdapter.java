package com.sky.chowder.model.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.sky.chowder.model.ActivityEntity;

import java.io.IOException;

/**
 * Created by libin on 2018/12/13 9:31 PM.
 */
public class ActivityAdapter extends TypeAdapter<ActivityEntity> {
    @Override
    public ActivityEntity read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        in.nextName();
        ActivityEntity entity = new ActivityEntity();
//        in.setLenient(true);
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "className":
                    entity.setName(in.nextString());
                    break;
                case "componentName":
                    entity.setComponent(in.nextString());
                    break;
                case "describe":
                    entity.setDes(in.nextString());
                    break;
                case "img":
                    entity.setImage(in.nextInt());
                    break;
                case "version":
                    entity.setVersion(in.nextDouble());
                    break;
                case "objList":
                    in.beginArray();
                    while (in.hasNext()) {
                        in.beginObject();
                        while (in.hasNext()) {
                            switch (in.nextName()) {
                                case "className":
                                    in.nextString();
                                    break;
                                case "componentName":
                                    in.nextString();
                                    break;
                                case "describe":
                                    in.nextString();
                                    break;
                                case "img":
                                    in.nextInt();
                                    break;
                            }
                        }
                        in.endObject();
                    }
                    in.endArray();
                    break;
            }
        }
        in.endObject();
        return entity;
    }

    @Override
    public void write(JsonWriter out, ActivityEntity value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.beginObject();
        out.name("className").value(value.getName());
        out.name("componentName").value(value.getComponent());
        out.name("describe").value(value.getDes());
        out.name("img").value(value.getImage());
        out.name("version").value(value.getVersion());
        out.endObject();
    }
}
