package com.fmr.findmyroom;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChatBotActivity extends AppCompatActivity {

    private List<ChatModel> chatList;
    private ListView chatListView;
    private Context thisContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        // init chat array list and chat list view
        chatList = new ArrayList<>();
        chatListView = findViewById(R.id.chatListView);

        // set the toolbar
        Toolbar chatBotToolbar = findViewById(R.id.chatBotToolbar);
        chatBotToolbar.setTitle("Chat Bot");
        setSupportActionBar(chatBotToolbar);

        // create AITextHandler instance
        final AITextHandler aiTextHandler = new AITextHandler();

        // create first chat
        chatList.add(new ChatModel("Hi, I can assist you in many ways. Anyway how can I help you now?", true));

        // setup list view
        ChatMessageAdapter chatMessageAdapter = new ChatMessageAdapter(chatList, thisContext);
        chatListView.setAdapter(chatMessageAdapter);

        // handle message sending button event
        FloatingActionButton msgSendBtn = findViewById(R.id.msgSendBtn);
        msgSendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // get the input message
                EditText inputMsgTxtView = findViewById(R.id.inputMessage);
                String inputMessage = inputMsgTxtView.getText().toString();

                // call to ai and set request
                aiTextHandler.setBotRequest(inputMessage);

                // set the request message to model
                chatList.add(new ChatModel(inputMessage, false));

                // reset input field
                inputMsgTxtView.setText("");

                // setup list view
                ChatMessageAdapter chatMessageAdapter = new ChatMessageAdapter(chatList, thisContext);
                chatListView.setAdapter(chatMessageAdapter);

                // set time out
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // get bot response
                        Map<String, Object> botResponse = aiTextHandler.getBotResponse();

                        if (botResponse != null) {
                            // get bot speech
                            String botSpeech = botResponse.get("botSpeech").toString();

                            // set the response message to model
                            chatList.add(new ChatModel(botSpeech, true));

                            // setup list view
                            ChatMessageAdapter chatMessageAdapter = new ChatMessageAdapter(chatList, thisContext);
                            chatListView.setAdapter(chatMessageAdapter);

                            // check for params
                            int paramCount = Integer.parseInt(botResponse.get("paramCount").toString());

                            if (paramCount == 2) {
                                findPropertyForResponse();
                            }
                        }
                    }
                }, 5000);
            }
        });
    }

    // navigate to room list view
    private void findPropertyForResponse() {
        final Intent toPropertyList = new Intent(thisContext, RoomListActivity.class);

        // wait
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(toPropertyList);
            }
        }, 3000);
    }
}
