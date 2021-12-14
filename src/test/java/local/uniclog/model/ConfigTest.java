package local.uniclog.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigTest {
    /**
     * Тестовый набор
     * {"name":"name","token":"token"}
     */
    @Test
    void configTest() {
        Config config = new Config("config_test.json");
        assertEquals(config.getName(), "name");
        assertEquals(config.getToken(), "token");
    }
}