package com.example.theaterchatappnew2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.theaterchatappnew2.R;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theaterchatappnew2.models.Message;

import java.util.List;

// MessageAdapter.java
public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Message> messages;
    public MessageAdapter(List<Message> messages) { this.messages = messages; }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isUser ? 1 : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = viewType == 1 ? R.layout.item_message_user : R.layout.item_message_bot;
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new RecyclerView.ViewHolder(view) {};
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView text = holder.itemView.findViewById(R.id.messageText);
        text.setText(messages.get(position).text);
    }

    @Override
    public int getItemCount() { return messages.size(); }
}