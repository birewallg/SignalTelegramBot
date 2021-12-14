package local.uniclog.service;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class DataProvider {

    protected <T>String save(T object) {
        return new Gson().toJson(object);
    }

    protected <T>T load(String json, Class<T> list) {
        return new Gson().fromJson(json, list);
    }

    protected <T>T load(String json, Type type) {
        return new Gson().fromJson(json, type);
    }
}
