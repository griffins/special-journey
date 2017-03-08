package io.gitlab.asyndicate.uhai;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Notification extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        GenericImageListAdapter adapter = new GenericImageListAdapter(R.layout.notifications);
        RecyclerView list = (RecyclerView) view.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter);

        ConversationItem item = new ConversationItem();
        item.setTitle("Wednesday 8");
        item.setPeek("You missed your morning pills");

        adapter.add(item);

        item = new ConversationItem();
        item.setTitle("General");
        item.setPeek("Your weight and glucose levels are outdated. Measure and record in the app");

        adapter.add(item);

        item = new ConversationItem();
        item.setTitle("Today's Diet");
        item.setPeek("35% fibre, 12% sugar, 16% Vitamins, Checkout the foods you need in the dietary section");

        adapter.add(item);

        item = new ConversationItem();
        item.setTitle("Configure phone");
        item.setPeek("Once you have a phone number attached to your account, you may choose to receive notifications delivered as SMS text messages alerting you to activity around your account");

        adapter.add(item);

    }
}
