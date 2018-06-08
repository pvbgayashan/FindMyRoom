package com.fmr.findmyroom.virtual_agent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fmr.findmyroom.R;
import com.github.library.bubbleview.BubbleTextView;
import java.util.List;

public class ChatMessageAdapter extends BaseAdapter {

    private List<ChatModel> chatList;
    private LayoutInflater layoutInflater;

    public ChatMessageAdapter(List<ChatModel> chatList, Context context) {
        this.chatList = chatList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int i) {
        return chatList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View currentView = view;

        // choose layout
        if (currentView == null) {
            if (chatList.get(i).isSend())
                currentView = layoutInflater.inflate(R.layout.list_sent_msg, null);
            else
                currentView = layoutInflater.inflate(R.layout.list_received_msg, null);
        }

        // set chat message to bubble view
        BubbleTextView bubbleTextView = currentView.findViewById(R.id.bubbleChat);
        bubbleTextView.setText(chatList.get(i).getChatMessage());

        return currentView;
    }
}