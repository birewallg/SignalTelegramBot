package local.uniclog.service;

import com.google.gson.reflect.TypeToken;
import local.uniclog.model.TelegramUser;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class DataManagement {
    // todo увести в конфиг
    private static final String filePath = "data.json";

    @Getter
    @Setter
    private List<TelegramUser> data = new LinkedList<>();

    public DataManagement() {
        this.load();
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
