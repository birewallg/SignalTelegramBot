package local.uniclog;

import com.google.gson.reflect.TypeToken;
import local.uniclog.model.TelegramUser;
import local.uniclog.service.DataProvider;

import java.util.List;

public class MainApp {

    public static void main(String[] args) {
        System.out.println("Object test:");
        var data = new DataProvider();
        var user = TelegramUser.builder().id(1231231231d).userName("Name").subscriber(true).build();
        var json = data.save(user);
        System.out.println("Json = " + json);
        var obj =  data.load(json, TelegramUser.class);
        System.out.println("Object = " + obj.toString());


        System.out.println("List test:");
        var userList = List.of(
                TelegramUser.builder().id(1231231231d).userName("Name1").subscriber(true).build(),
                TelegramUser.builder().id(1231231231d).userName("Name2").subscriber(false).build()
        );
        json = data.save(userList);
        System.out.println("Json = " + json);
        var list =  data.load(json, new TypeToken<List<TelegramUser>>() {}.getType());
        System.out.println("Object = " + list.toString());
    }
}
