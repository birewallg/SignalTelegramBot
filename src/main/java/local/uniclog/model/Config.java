package local.uniclog.model;

import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;

/**
 * Конфиг для бота
 * json: { "name": "name", "token": "token" }
 */
@Data
@Slf4j
public class Config {
    private String name;
    private String token;

    public Config(String path) {
        if (path != null) {
            try (var reader = new FileReader(path)) {
                var config = new Gson().fromJson(reader, Config.class);
                this.name = config.getName();
                this.token = config.getToken();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
