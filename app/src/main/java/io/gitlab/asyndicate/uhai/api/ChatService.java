package io.gitlab.asyndicate.uhai.api;


import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import io.gitlab.asyndicate.uhai.Settings;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatService extends IntentService {
    static Socket io;

    public ChatService() {
        super("ChatIO");
    }

    void start() throws URISyntaxException {
        io = IO.socket("http://10.1.5.44:8080");
        io.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            public void call(Object... args) {
                setNick();
                JSONObject message = new JSONObject();
                try {
                    message.put("type", "people");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                io.emit("message", message);
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            public void call(Object... args) {

            }
        }).on("message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject response = (JSONObject) args[0];
                Intent intent = new Intent();
                intent.putExtra("message", response.toString());
                intent.setAction("message");
                sendBroadcast(intent);
                Log.d("Messages", "Sending intent");
            }
        });
        io.connect();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (io == null) {
            try {
                start();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            if (intent.getAction().equalsIgnoreCase("people")) {
                JSONObject message = new JSONObject();
                try {
                    message.put("type", "people");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                io.emit("message", message);
            } else {
                if (intent.getAction().equalsIgnoreCase("nick")) {
                    setNick();
                } else if (intent.getAction().equalsIgnoreCase("send")) {
                    JSONObject message = new JSONObject();
                    try {
                        message.put("type", "text");
                        message.put("payload", intent.getStringExtra("message"));
                        message.put("destination", intent.getStringExtra("destination"));
                        io.emit("message", message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (intent.getAction().contentEquals("photo")) {
                    File originalFile = new File(intent.getStringExtra("file"));
                    String encodedBase64;
                    try {
                        FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
                        byte[] bytes = new byte[(int) originalFile.length()];
                        fileInputStreamReader.read(bytes);
                        encodedBase64 = new String(Base64.encode(bytes, 0));
                        JSONObject message = new JSONObject();
                        try {
                            message.put("type", "decode");
                            message.put("payload", encodedBase64);
                            io.emit("message", message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void setNick() {
        JSONObject message = new JSONObject();
        try {
            message.put("type", "nick");
            message.put("payload", Settings.getString("name", "Guest " + (int) (Math.random() * 1000)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        io.emit("message", message);
    }
}
