package local.uniclog.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import local.uniclog.model.TelegramUser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DataManagement {
    // todo увести в конфиг
    private final String filePath;

    @Getter
    private ArrayList<TelegramUser> data;

    public DataManagement(String path) {
        this.filePath = path;
        this.data = new ArrayList<>();
        this.load();
    }

    public void setData(ArrayList<TelegramUser> data) {
        this.data = data;
        this.save();
    }

    public boolean add(TelegramUser model) {
        this.data.add(model);
        return this.save() || !data.remove(model);
    }

    public boolean remove(TelegramUser model) {
        new ArrayList<>(data).stream()
                .filter(user -> user.getId().equals(model.getId()))
                .forEach(user -> data.remove(user));
        return this.save();
    }

    public boolean update(TelegramUser model) {
        new ArrayList<>(data).stream()
                .filter(user -> user.getId().equals(model.getId()))
                .forEach(user -> {
                    data.remove(user);
                });
        return this.add(model);
    }

    public boolean save() {
        try (var writer = new FileWriter(filePath)) {
            writer.write(new Gson().toJson(data));
            return true;
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public boolean load() {
        try (var reader = new FileReader(filePath)) {
            this.data = new Gson().fromJson(reader, new TypeToken<List<TelegramUser>>() {
            }.getType());
            return true;
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
