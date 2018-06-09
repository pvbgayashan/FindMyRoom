package com.fmr.findmyroom.virtual_agent;

import android.os.AsyncTask;

import com.google.gson.JsonElement;

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
    private HashMap<String, JsonElement> params;

    public AITextHandler() {
        // client access token
        final String CLIENT_ACCESS_TOKEN = "f63af3565d55449b9946e7a8d298b07a";

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
        // print user message on console
        System.out.println("Send: " + userInputMsg);

        // set user input to query
        aiRequest.setQuery(userInputMsg);

        // do the request through async task
        new RequestTaskHandler(this).execute(aiRequest);
    }

    @Override
    public void setBotResponse(String botSpeech, HashMap<String, JsonElement> params) {
        // print bot response message on console
        System.out.println("Receive: " + botSpeech);

        // assign response
        this.botSpeech = botSpeech;
        this.params = params;
    }

    // handling the response
    public Map<String, Object> getBotResponse() {
        Map<String, Object> resultMap = new HashMap<>();

        // put data to map
        resultMap.put("botSpeech", botSpeech);
        resultMap.put("params", params);

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
            } catch (AIServiceException ex) {
                ex.printStackTrace();
            }

            return aiResponse;
        }

        @Override
        protected void onPostExecute(AIResponse aiResponse) {
            Result result;
            if (aiResponse != null && aiResponse.getResult() != null)
                result = aiResponse.getResult();
            else
                return;

            String botSpeech = result.getFulfillment().getSpeech();
            HashMap<String, JsonElement> params = result.getParameters();

            asyncResponse.setBotResponse(botSpeech, params);
        }
    }
}

// create an interface to get the output
interface AsyncResponse {
    void setBotResponse(String botSpeech, HashMap<String, JsonElement> params);
}
