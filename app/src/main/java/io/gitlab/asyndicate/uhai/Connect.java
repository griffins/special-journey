package io.gitlab.asyndicate.uhai;

import android.content.Context;
import android.content.Intent;
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

import io.gitlab.asyndicate.uhai.api.ChatService;

public class Connect extends Fragment implements HomeAccess {
    GenericImageListAdapter adapter;
    private Home home;
    Chat chat;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        adapter = new GenericImageListAdapter(R.layout.conversation);
        RecyclerView list = (RecyclerView) view.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter);
    }

    @Override
    public void setHomeActivity(Home home) {
        this.home = home;
    }

    public void update(String message) {
        try {
            JSONObject object = new JSONObject(message);
            if (object.getString("type").equalsIgnoreCase("nicks")) {
                adapter.clear();
                JSONArray people = object.getJSONArray("payload");
                for (int x = 0; x < people.length(); x++) {
                    JSONObject person = people.getJSONObject(x);
                    final ConversationItem item = new ConversationItem();
                    item.setTitle(person.getString("name"));
                    item.setPeek(person.getString("ip"));
                    item.setAction(new PayloadRunnable() {
                        @Override
                        public Object run(Object result) {
                            chat = new Chat();
                            chat.setHomeActivity(home);
                            home.setTitle(item.getTitle());
                            chat.setConversation(item.getPrimaryText());
                            chat.setConversationIp(item.getSecondaryText());
                            home.getSupportFragmentManager().beginTransaction().replace(R.id.container, chat).commit();
                            return null;
                        }
                    });
                    adapter.add(item);
                }
            } else if (object.getString("type").equalsIgnoreCase("text") && chat != null) {
                chat.update(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Intent intent = new Intent(context, ChatService.class);
        intent.setAction("people");
        context.startService(intent);
    }
}
