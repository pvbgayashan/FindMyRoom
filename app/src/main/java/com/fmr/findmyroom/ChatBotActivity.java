package com.fmr.findmyroom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ChatBotActivity extends AppCompatActivity {

    List<ChatModel> firstChat = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        setUpMessage();

        ListView chatListView = findViewById(R.id.chatListView);
        ChatMessageAdapter chatMessageAdapter = new ChatMessageAdapter(firstChat, this);
        chatListView.setAdapter(chatMessageAdapter);
    }

    private void setUpMessage() {
        firstChat.add(new ChatModel("Hello, How can I help you?", true));
        firstChat.add(new ChatModel("I need a place to stay", false));
        firstChat.add(new ChatModel("Okay, where are you now?", true));
        firstChat.add(new ChatModel("Now I'm Panadura", false));
        firstChat.add(new ChatModel("Fine, give me a second to find you a place to stay", true));
        firstChat.add(new ChatModel("...", true));
    }
}
