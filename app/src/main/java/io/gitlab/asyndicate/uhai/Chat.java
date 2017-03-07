package io.gitlab.asyndicate.uhai;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Chat extends Fragment implements HomeAccess {
    ChatListAdapter adapter;
    Home home;
    private String conversation;
    private String conversationIp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        adapter = new ChatListAdapter(R.layout.chat);
        return inflater.inflate(R.layout.list, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView list = (RecyclerView) view.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        list.setAdapter(adapter);
        adapter.setConversationIp(conversationIp);
    }

    @Override
    public void setHomeActivity(Home home) {
        this.home = home;
    }

    public void setConversation(String conversation) {
        this.conversation = conversation;
    }

    public String getConversation() {
        return conversation;
    }

    public void setConversationIp(String conversationIp) {
        this.conversationIp = conversationIp;
    }

    public void update(String message) {
        Log.d("Messages", message);
        try {
            JSONObject object = new JSONObject(message);
            if (object.getString("type").equalsIgnoreCase("text")) {
                if (object.getString("from").equalsIgnoreCase(conversationIp)) {
                    ConversationItem item = new ConversationItem();
                    item.setTitle(object.getString("payload"));
                    item.setPeek((new Date()).toString());
                    adapter.add(item);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
