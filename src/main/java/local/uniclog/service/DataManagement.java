package local.uniclog.service;

import com.google.gson.reflect.TypeToken;
import local.uniclog.model.TelegramUser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class DataManagement {
    // todo увести в конфиг
    private static final String filePath = "data.json";

    @Getter
    private ArrayList<TelegramUser> data;

    public DataManagement() {
        data = new ArrayList<>();
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
                .filter(user -> user.getId() == model.getId())
                .forEach(user -> data.remove(user));
        return this.save();
    }

    public boolean save() {
        try (var writer = new FileWriter(filePath)) {
            writer.write(new DataProvider().save(data));
            return true;
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public boolean load() {
        try (var reader = new FileReader(filePath);
             var scanner = new Scanner(reader)) {
            var json = new StringBuilder();
            while (scanner.hasNext())
                json.append(scanner.next());
            this.data = new DataProvider().load(
                    json.toString(),
                    new TypeToken<List<TelegramUser>>() {
                    }.getType());
            return true;
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
