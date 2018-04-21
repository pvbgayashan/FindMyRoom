package com.fmr.findmyroom;

import android.os.AsyncTask;

import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;

/**
 * Created by pvbgayashan on 4/11/18.
 */

public class AITextHandler implements AsyncResponse {

    private AIDataService aiDataService;
    private AIRequest aiRequest;
    private String botSpeech;

    public AITextHandler() {
        // client access token
        final String CLIENT_ACCESS_TOKEN = "783e722978d140c7a9a3b2add252c362";

        // create ai configuration instance
        final AIConfiguration aiConfig = new AIConfiguration(CLIENT_ACCESS_TOKEN,
                AIConfiguration.SupportedLanguages.English, AIConfiguration.RecognitionEngine.System);

        // create ai data service instance
        aiDataService = new AIDataService(aiConfig);

        // create ai request instance
        aiRequest = new AIRequest();
    }

    // handling the request
    public void setBotRequest(String userInputMsg) {

        System.out.println("Send: " + userInputMsg);

        // set user input to query
        aiRequest.setQuery(userInputMsg);

        // do the request through async task
        new RequestTaskHandler(this).execute(aiRequest);
    }

    @Override
    public void setBotResponse(String output) {
        botSpeech = output;
        System.out.println("Receive: " + output);
    }

    public String getBotResponse() {
        return botSpeech;
    }

    // pass ai request as async task
    public class RequestTaskHandler extends AsyncTask<AIRequest, Void, AIResponse> {

        private AsyncResponse asyncResponse = null;

        private RequestTaskHandler(AsyncResponse asyncResponse) {
            this.asyncResponse = asyncResponse;
        }

        @Override
        protected AIResponse doInBackground(AIRequest... aiRequests) {
            AIRequest aiRequest = aiRequests[0];
            AIResponse aiResponse = null;

            try {
                aiResponse = aiDataService.request(aiRequest);
            } catch (AIServiceException e) {
                e.printStackTrace();
            }

            return aiResponse;
        }

        @Override
        protected void onPostExecute(AIResponse aiResponse) {
            asyncResponse.setBotResponse(aiResponse.getResult().getFulfillment().getSpeech());
        }
    }
}

// create an interface to get the output
interface AsyncResponse {
    void setBotResponse(String output);
}
