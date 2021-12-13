package local.uniclog.service;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class DataProvider {

    public <T>String save(T object) {
        return new Gson().toJson(object);
    }

    public <T>T load(String json, Class<T> list) {
        return new Gson().fromJson(json, list);
    }

    public <T>T load(String json, Type list) {
        return new Gson().fromJson(json, list);
    }
}
