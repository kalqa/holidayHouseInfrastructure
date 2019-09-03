package client;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;

public class NotificationListener implements Runnable {

    private String nickName;

    public NotificationListener(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public void run() {
        EventHandler eventHandler = new SimpleEventHandler();
        String url = String.format("http://localhost:9090/notification?nickName=" + nickName);
        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(url));

        try (EventSource eventSource = builder.build()) {
            eventSource.setReconnectionTimeMs(3000);
            eventSource.start();

            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
