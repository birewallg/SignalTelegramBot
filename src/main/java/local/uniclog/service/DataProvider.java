package local.uniclog.service;

import com.google.gson.Gson;

import java.lang.reflect.Type;

@Deprecated
public class DataProvider {

    protected <T>String save(T object) {
        return new Gson().toJson(object);
    }

    protected <T>T load(String json, T type) {
        return new Gson().fromJson(json, (Type) type);
    }

    protected <T>T load(String json, Type type) {
        return new Gson().fromJson(json, type);
    }
}
