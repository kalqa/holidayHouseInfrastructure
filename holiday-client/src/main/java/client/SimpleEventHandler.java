package client;

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
        System.out.println("========= " + event);
        String data = messageEvent.getData();
        System.out.println("data======== " + data);
//        if (event.equals("message")) {
//            System.out.println("event NAME: " + event);
//            System.out.println("ON MESSAGE:");
//            System.out.println(messageEvent.getData());
//        } else {
//            System.out.println("event NAME: " + event);
//        }
    }

    @Override
    public void onComment(String comment) {
    }

    @Override
    public void onError(Throwable t) {
//        System.out.println("onError: " + t);
    }
}