package com.fmr.findmyroom;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatBotActivity extends AppCompatActivity {

    private List<ChatModel> chatList;
    private ListView chatListView;
    private Context thisContext = this;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        // init fire base auth instance
        mAuth = FirebaseAuth.getInstance();

        // init chat array list and chat list view
        chatList = new ArrayList<>();
        chatListView = findViewById(R.id.chatListView);

        // set the toolbar
        Toolbar chatBotToolbar = findViewById(R.id.chatBotToolbar);
        chatBotToolbar.setTitle(R.string.bot_name);
        setSupportActionBar(chatBotToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                            HashMap<String, JsonElement> params;
                            if (botResponse.get("params") != null && botResponse.get("params") instanceof HashMap) {
                                params = (HashMap<String, JsonElement>) botResponse.get("params");

                                // all necessary params provided
                                if (params.size() == 2) {
                                    findPropertyForResponse(params);
                                }
                            }
                        }
                    }
                }, 5000);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.logoutOption:
                mAuth.signOut();
                break;
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    // navigate to room list view
    private void findPropertyForResponse(HashMap<String, JsonElement> params) {
        final Intent toPropertyListIntent = new Intent(thisContext, RoomListActivity.class);

        // put data to intent
        for (Map.Entry entry : params.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();

            toPropertyListIntent.putExtra(key, value);
        }

        // wait
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(toPropertyListIntent);
            }
        }, 5000);
    }
}
