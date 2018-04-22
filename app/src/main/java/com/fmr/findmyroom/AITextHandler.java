package com.fmr.findmyroom;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;

import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class AITextHandler implements AsyncResponse {

    private AIDataService aiDataService;
    private AIRequest aiRequest;
    private String botSpeech;
    private int paramCount;

    public AITextHandler() {
        // client access token
        final String CLIENT_ACCESS_TOKEN = "0e3c85ca7b3f4d489e339f01400b75ec";

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
    public void setBotResponse(String botSpeech, int paramCount) {
        this.botSpeech = botSpeech;
        this.paramCount = paramCount;

        System.out.println("Receive: " + botSpeech);
    }

    public Map<String, Object> getBotResponse() {
        Map<String, Object> resultMap = new HashMap<>();

        // put data
        resultMap.put("botSpeech", botSpeech);
        resultMap.put("paramCount", paramCount);

        return resultMap;
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
            Result result = aiResponse.getResult();

            String botSpeech = result.getFulfillment().getSpeech();
            int paramCount = result.getParameters().size();

            asyncResponse.setBotResponse(botSpeech, paramCount);
        }
    }
}

// create an interface to get the output
interface AsyncResponse {
    void setBotResponse(String botSpeech, int paramCount);
}
