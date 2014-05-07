package edu.berkeley.cs160.lasercats;

import android.content.Context;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;

public class ContinuousRecognizer implements RecognitionListener {

    public static final String TAG = "ContinuousRecognizer";
    private Context mContext;
    private SpeechRecognizer mRecognizer;
    private boolean mIsListening = false;
    private ContinuousRecognizerCallback mCallback;

    public interface ContinuousRecognizerCallback {
        void onResult(String result);
    }

    public ContinuousRecognizer(Context context) {
        mContext = context;
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(mContext);
        mRecognizer.setRecognitionListener(this);
    }

    public void setContinuousRecognizerCallback(ContinuousRecognizerCallback cb) {
        mCallback = cb;
    }


    public void startListening() {
        mIsListening = true;
        listen();
    }

    public void stopListening() {
        mIsListening = false;
        mRecognizer.stopListening();
        mRecognizer.cancel();
    }

    private void listen() {
        if (mIsListening) {
            mRecognizer.startListening(RecognizerIntent.getVoiceDetailsIntent(mContext));
        }
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onBufferReceived(byte[] arg0) {
        Log.d(TAG, "onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
        Log.d(TAG, "onEndOfSpeech");
    }

    @Override
    public void onError(int error) {
        Log.e(TAG, "error: " + String.valueOf(error));
        String mError = "";
        switch (error) {
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                mError = " network timeout";
                startListening();
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                mError = " network" ;
                return;
            case SpeechRecognizer.ERROR_AUDIO:
                mError = " audio";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                mError = " server";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                mError = " client";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                mError = " speech time out" ;
                startListening();
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                mError = " no match" ;
                startListening();
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                mError = " recogniser busy" ;
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                mError = " insufficient permissions" ;
                break;
        }
        Log.e(TAG,  "Error: " +  error + " - " + mError);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.d(TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.d(TAG, "onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.d(TAG, "onReadyForSpeech");
    }

    @Override
    public void onResults(Bundle results) {
        Log.d(TAG, "onResults");
        ArrayList<String> strings = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        for (String str : strings) {
            Log.d(TAG, "result=" + str);
        }

        // only sends the first result to the callback- this can be modified if necessary
        if (mCallback != null) {
            mCallback.onResult(strings.get(0));
        }

        // continue listening
        listen();
    }

    @Override
    public void onRmsChanged(float arg0) {
        Log.d(TAG, "onRmsChanged");
    }

}