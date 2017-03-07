package io.gitlab.asyndicate.uhai;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.gitlab.asyndicate.uhai.api.ChatService;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private ArrayList<GenericListItemInterface> mDataset;
    private int layoutId;
    private boolean stripped;
    String conversationIp;

    public ChatListAdapter(int layoutId) {
        mDataset = new ArrayList<>();
        this.layoutId = layoutId;
    }

    public ChatListAdapter(ArrayList<GenericListItemInterface> dataset, int layoutId) {
        mDataset = dataset;
        this.layoutId = layoutId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0) {
            convertView = inflater.inflate(this.layoutId, parent, false);
        } else {
            convertView = inflater.inflate(R.layout.chatter, parent, false);
        }
        return new ViewHolder(convertView, viewType);
    }

    public void add(GenericListItemInterface item) {
        mDataset.add(item);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (getItemViewType(position) == 0) {
            holder.primaryText.setText(getItem(position).getPrimaryText());
            holder.secondaryText.setText(getItem(position).getSecondaryText());

            if (position % 2 == 0) {
                holder.getView().setBackgroundColor(holder.getView().getContext().getResources().getColor(R.color.divider));
            } else {
                holder.getView().setBackgroundColor(Color.WHITE);
            }

            holder.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getItem(position).getAction() != null) {
                        getItem(position).getAction().run(true);
                    }
                }
            });

        }
    }

    private GenericListItemInterface getItem(int position) {
        return mDataset.get(position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mDataset.size()) {
            return 0;
        } else {
            return 1;
        }
    }

    public void clear() {
        mDataset.clear();
        notifyDataSetChanged();
    }

    public boolean isStripped() {
        return stripped;
    }

    public void setStripped(boolean stripped) {
        this.stripped = stripped;
    }

    public void setConversationIp(String conversationIp) {
        this.conversationIp = conversationIp;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView primaryText;
        TextView secondaryText;
        protected View view;
        ImageView primaryImage;

        ViewHolder(View view, int viewType) {
            super(view);
            this.view = view;
            if (viewType == 0) {
                primaryText = (TextView) view.findViewById(R.id.primaryText);
                secondaryText = (TextView) view.findViewById(R.id.secondaryText);
                primaryImage = (ImageView) view.findViewById(R.id.primaryImage);
            } else {
                final EditText mesaage = (EditText) view.findViewById(R.id.message);
                Button btn = (Button) view.findViewById(R.id.send);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), ChatService.class);
                        intent.setAction("send");
                        intent.putExtra("message", mesaage.getText().toString());
                        intent.putExtra("destination", ChatListAdapter.this.conversationIp);
                        view.getContext().startService(intent);
                        mesaage.setText("");
                    }
                });
            }
        }

        public View getView() {
            return view;
        }
    }
}