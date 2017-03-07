package io.gitlab.asyndicate.uhai;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class GenericImageListAdapter extends RecyclerView.Adapter<GenericImageListAdapter.ViewHolder> {

    private ArrayList<GenericListItemInterface> mDataset;
    private int layoutId;
    private boolean stripped;

    public GenericImageListAdapter(int layoutId) {
        mDataset = new ArrayList<>();
        this.layoutId = layoutId;
    }

    public GenericImageListAdapter(ArrayList<GenericListItemInterface> dataset, int layoutId) {
        mDataset = dataset;
        this.layoutId = layoutId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        convertView = inflater.inflate(this.layoutId, parent, false);
        return new ViewHolder(convertView);
    }

    public void add(GenericListItemInterface item) {
        mDataset.add(item);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.primaryText.setText(getItem(position).getPrimaryText());
        holder.secondaryText.setText(getItem(position).getSecondaryText());
        if (getItem(position).getPrimaryImage() != null) {
            holder.primaryImage.setImageDrawable(getItem(position).getPrimaryImage());
        }
        if (isStripped()) {
            if (position % 2 == 0) {
                holder.getView().setBackgroundColor(holder.getView().getContext().getResources().getColor(R.color.colorAccent));
            } else {
                holder.getView().setBackgroundColor(Color.WHITE);
            }
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

    private GenericListItemInterface getItem(int position) {
        return mDataset.get(position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView primaryText;
        TextView secondaryText;
        protected View view;
        ImageView primaryImage;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            primaryText = (TextView) view.findViewById(R.id.primaryText);
            secondaryText = (TextView) view.findViewById(R.id.secondaryText);
            primaryImage = (ImageView) view.findViewById(R.id.primaryImage);
        }

        public View getView() {
            return view;
        }
    }
}