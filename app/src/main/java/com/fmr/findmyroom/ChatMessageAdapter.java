package com.fmr.findmyroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.github.library.bubbleview.BubbleTextView;

import java.util.List;

/**
 * Created by buddhika on 3/16/18.
 */

public class ChatMessageAdapter extends BaseAdapter {

    private List<ChatModel> firstChat;
    private Context context;
    private LayoutInflater layoutInflater;

    public ChatMessageAdapter(List<ChatModel> firstChat, Context context) {
        this.firstChat = firstChat;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return firstChat.size();
    }

    @Override
    public Object getItem(int i) {
        return firstChat.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View currentView = view;

        if (currentView == null) {
            if (firstChat.get(i).isSend())
                currentView = layoutInflater.inflate(R.layout.list_sent_msgs, null);
            else
                currentView = layoutInflater.inflate(R.layout.list_received_msgs, null);
        }

        BubbleTextView bubbleTextView = currentView.findViewById(R.id.bubbleChat);
        bubbleTextView.setText(firstChat.get(i).getChatMessage());

        return currentView;
    }
}
