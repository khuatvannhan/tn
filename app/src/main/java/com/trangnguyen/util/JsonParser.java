package com.trangnguyen.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonParser {
    public String id;
    public String name;
    JsonParser(){

    }
    JsonParser(String _id, String _name){
        this.id = _id;
        this.name = _name;
    }
    public void setId(String _id){
        this.id = _id;
    }
    public String getId(){
        return this.id;
    }

    final String TAG = "JsonParser.java";

}