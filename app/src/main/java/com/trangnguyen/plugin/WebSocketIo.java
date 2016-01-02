package com.trangnguyen.plugin;

import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by snowflower on 27/12/2015.
 */
public class WebSocketIo {
    public static Socket mSocket;
    public static boolean isServerReady = false;
    public static void connect(){
        try {
            mSocket = IO.socket(Constants.CONNECT_SOCKET_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean isConnectSocket() {
        mSocket.on("server-ready", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    isServerReady = true;
                    JSONObject infor = new JSONObject();
                    infor.put("device", "B4");
                    Ack ack = (Ack) args[args.length - 1];
                    ack.call("client-ready");
                } catch (JSONException e){
                    isServerReady = false;
                    e.printStackTrace();
                }
            }
        });
        return isServerReady;
    }
    public static void logIn(){
        try {
            JSONObject jp = new JSONObject();
            jp.put("use", "FZG1RRRHXJ");
            jp.put("pass", "123456");
            mSocket.emit("login", jp, new Ack() {
                @Override
                public void call(Object... args) {
                    Log.d("CALLBACK", " == " + args[0]);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void SignUp(JSONObject jpSignUp){
        try {
            mSocket.emit("login", jpSignUp, new Ack() {
                @Override
                public void call(Object... args) {
                    Log.d("Use", " == " + args[0]);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject sendInforTest() {
        JSONObject jpInformation = new JSONObject();
        try {
            JSONObject jp = new JSONObject();
            jp.put("name_test", "abc");
            jp.put("time_test", "FZG1RRRHXJ");
            jp.put("answer_true", "123456");
            jp.put("answer_false", "");
            mSocket.emit("login", jp, new Ack() {
                @Override
                public void call(Object... args) {
                    Log.d("Use", " == " + args[0]);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jpInformation;
    }

    public static JSONObject getInformationUse(){
        JSONObject jpInformation = new JSONObject();
        try {
            JSONObject jp = new JSONObject();
            jp.put("card_number", "FZG1RRRHXJ");
            jp.put("pass", "123456");
            mSocket.emit("login", jp, new Ack() {
                @Override
                public void call(Object... args) {
                    Log.d("Use", " == " + args[0]);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jpInformation;
    }

}
