package client;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;

public class SimpleEventHandler implements EventHandler {

    @Override
    public void onOpen() {
    }

    @Override
    public void onClosed() {
    }

    @Override
    public void onMessage(String event, MessageEvent messageEvent) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(messageEvent.getData()))) {
            JsonObject jsonObject = jsonReader.readObject();
            JsonValue message = jsonObject.getValue("/message");
            System.out.println(message);
        }
    }

    @Override
    public void onComment(String comment) {
    }

    @Override
    public void onError(Throwable t) {
    }
}