package com.fmr.findmyroom;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChatBotActivity extends AppCompatActivity {

    private List<ChatModel> firstChat = new ArrayList<>();
    private Context thisContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        // set up initial message list
        setUpMessage();

        // setup list view
        ListView chatListView = findViewById(R.id.chatListView);
        ChatMessageAdapter chatMessageAdapter = new ChatMessageAdapter(firstChat, thisContext);
        chatListView.setAdapter(chatMessageAdapter);

        // handle message sending button event
        FloatingActionButton msgSendBtn = findViewById(R.id.msgSendBtn);
        msgSendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // get the input message
                TextView inputMsgTxtView = findViewById(R.id.inputMessage);
                String inputMessage = inputMsgTxtView.getText().toString();

                // set the message to model
                firstChat.add(new ChatModel(inputMessage, false));

                // setup list view
                ListView chatListView = findViewById(R.id.chatListView);
                ChatMessageAdapter chatMessageAdapter = new ChatMessageAdapter(firstChat, thisContext);
                chatListView.setAdapter(chatMessageAdapter);
            }
        });
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
