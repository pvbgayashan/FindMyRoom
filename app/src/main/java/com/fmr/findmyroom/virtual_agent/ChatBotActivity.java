package com.fmr.findmyroom.virtual_agent;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.fmr.findmyroom.R;
import com.fmr.findmyroom.view_property.PropertyListActivity;
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
        String firstMessage = "Hi, How can I help you?";

        // set chat view
        setChatView(firstMessage, false);

        // handle message sending button event
        final FloatingActionButton msgSendBtn = findViewById(R.id.msgSendBtn);
        msgSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the input message
                EditText inputMsgTxt = findViewById(R.id.inputMessageTxt);
                String inputMessage = inputMsgTxt.getText().toString();

                // call to ai and set request
                aiTextHandler.setBotRequest(inputMessage);

                // set chat view
                setChatView(inputMessage, true);

                // reset input field
                inputMsgTxt.setText("");

                // set time out
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // get bot response
                        Map<String, Object> botResponse = aiTextHandler.getBotResponse();

                        if (botResponse != null) {
                            // get bot speech
                            String botSpeech;
                            if (botResponse.get("botSpeech") != null)
                                botSpeech = botResponse.get("botSpeech").toString();
                            else
                                botSpeech = "Sorry, please ask it again";

                            // set chat view
                            setChatView(botSpeech, false);

                            // check for params
                            final HashMap<String, JsonElement> params;
                            if (botResponse.get("params") != null && botResponse.get("params") instanceof HashMap) {
                                params = (HashMap<String, JsonElement>) botResponse.get("params");

                                // find property with parameters
                                if (params.size() == 4) {
                                    Snackbar snackbar = Snackbar.make(msgSendBtn, R.string.snack_bar_text, Snackbar.LENGTH_INDEFINITE);
                                    snackbar.setAction(R.string.confirm, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            findPropertyForResponse(params);
                                        }
                                    });
                                    snackbar.show();
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
        menuInflater.inflate(R.menu.menu_general, menu);
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

    // set and view messages in chat view
    private void setChatView(String message, boolean isSend) {
        // set the message to model
        chatList.add(new ChatModel(message, isSend));

        // setup list view
        ChatMessageAdapter chatMessageAdapter = new ChatMessageAdapter(chatList, thisContext);
        chatListView.setAdapter(chatMessageAdapter);
    }

    // navigate to room list view
    private void findPropertyForResponse(HashMap<String, JsonElement> params) {
        final Intent toPropertyListIntent = new Intent(thisContext, PropertyListActivity.class);

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
                finish();
                startActivity(toPropertyListIntent);
            }
        }, 200);
    }
}
