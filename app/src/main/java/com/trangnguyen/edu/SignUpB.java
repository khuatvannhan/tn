package com.trangnguyen.edu;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.trangnguyen.util.ParamUtil;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import android.widget.ListPopupWindow;

/**
 * Created by snowflower on 29/12/2015.
 */
public class SignUpB extends Activity implements View.OnTouchListener,
        AdapterView.OnItemClickListener {
    final static String TAG = "SignUpB";
    final String urlListJsonCity = "http://trangnguyen.edu.vn/province/api/select_list";
    String urlListJsonDistrict = "http://trangnguyen.edu.vn/district/api/list/";
    String urlListJsonSchool = "http://trangnguyen.edu.vn/school/api/list/";

    public static JSONObject jObject; // json
    public static JSONArray data; // get data object
    public ParamUtil paramUtil = new ParamUtil();
    private ListPopupWindow lpw;
    public boolean isCity = true, isDistrict = false, isSchool = false, isComplete = false;

    HashMap<String, String> hashCity;
    HashMap<String, String> hashDistrict;
    HashMap<String, String> hashSchool;
    static EditText edCity;
    static EditText edDistrict;
    static EditText edNameSchool;
    String[] listCityName;
    String[] listDistrictName;
    String[] listSchoolName;
    Button btSignUp;
    String item = "";
    String key = "";
    static int idEditText = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_b);
        init();
        handerOnclick();
        setJsonListCity(urlListJsonCity, 0);
        //showLocatioin();

        if (isConnected()) {
            Log.d("Tag", "You are conncted");
        } else {
            Log.d("Tag", "You are disconnect");
        }
    }

    public static Object getKeyFromValue(HashMap hm, Object value) {
        for (Object key : hm.keySet()) {
            if (hm.get(key).equals(value)) {
                return key;
            }
        }
        return null;
    }

    public String[] getNameList(HashMap<String, String> hashMap) {
        String[] list = new String[hashMap.size()];
        int length = hashMap.size();
        int index = 0;
        for (int i = 0; i < length; i++) {
            if ((hashMap.get(String.valueOf(i)) != null) && (hashMap != null)) {
                list[index] = hashMap.get(String.valueOf(i));
                index++;
            } else if ((hashMap.get(String.valueOf(i)) == null) && (hashMap != null)) {
                length++;
            }
        }
        return list;
    }

    public void setlpw(String[] list, EditText editText) {
        lpw = null;
        lpw = new ListPopupWindow(this);
        lpw.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list));
        lpw.setAnchorView(editText);
        lpw.setModal(true);
        lpw.setOnItemClickListener(this);
    }

    public void checksetlpw(View v) {
        if (v.getId() == edCity.getId()) {
            if (listCityName != null) {
                setlpw(listCityName, edCity);
            }
        }
        if (v.getId() == edDistrict.getId()) {
            if (listDistrictName != null) {
                setlpw(listDistrictName, edDistrict);
            }
        }
        if (v.getId() == edNameSchool.getId()) {
            if (listSchoolName != null) {
                setlpw(listSchoolName, edNameSchool);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        idEditText = v.getId();
        checksetlpw(v);
        final int DRAWABLE_RIGHT = 2;
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getX() >= (v.getWidth() - ((EditText) v)
                    .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                if (lpw != null) {
                    lpw.show();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        try {
            if (idEditText == edCity.getId()) {
                item = listCityName[position];
                key = String.valueOf(getKeyFromValue(hashCity, listCityName[position]));
                edCity.setText(item);
                isDistrict = true;
                setJsonListDistrict(urlListJsonDistrict, key);
                Log.e(TAG, " ID EditText  City = " + edCity.getId());
            }
            if (idEditText == edDistrict.getId()) {
                item = listDistrictName[position];
                key = String.valueOf(getKeyFromValue(hashDistrict, listDistrictName[position]));
                edDistrict.setText(item);
                isSchool = true;
                setJsonListSchool(urlListJsonSchool, key);
                Log.e(TAG, " ID EditText = " + edDistrict.getId());
            }
            if (idEditText == edNameSchool.getId()) {
                item = listSchoolName[position];
                key = String.valueOf(getKeyFromValue(hashSchool, listSchoolName[position]));
                edNameSchool.setText(item);
                Log.e(TAG, " ID EditText = " + edNameSchool.getId());
            }
            if (lpw != null) {
                lpw.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handerOnclick() {
        edCity.setOnTouchListener(this);
        edDistrict.setOnTouchListener(this);
        edNameSchool.setOnTouchListener(this);
        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpB.this, Home.class);
                startActivity(intent);
            }
        });
    }

    public static String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        try {
            jObject = new JSONObject(result);
            if ((data == null) || (data.equals(""))) {
                data = jObject.getJSONArray("content");
                Log.d(TAG, " data = " + data.get(0).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("TAG", "result ====== " + result);
        return result;
    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            isComplete = true;
            if (data != null) {
                if (isCity) {
                    isCity = false;
                    listCityName = null;
                    setHashListCity(data);
                    reset();
                    listCityName = getNameList(hashCity);
                    setlpw(listCityName, edCity);
                }
                if (isDistrict) {
                    isDistrict = false;
                    listDistrictName = null;
                    setHashListDistrict(data);
                    reset();
                    listDistrictName = getNameList(hashDistrict);
                    setlpw(listDistrictName, edDistrict);
                }
                if (isSchool) {
                    isSchool = false;
                    listSchoolName = null;
                    setHashListSchool(data);
                    reset();
                    listSchoolName = getNameList(hashSchool);
                    setlpw(listSchoolName, edNameSchool);
                }
            }
        }
    }

    public void reset() {
        data = null;
    }

    public void init() {
        edCity = (EditText) findViewById(R.id.edCity);
        edDistrict = (EditText) findViewById(R.id.edDistrict);
        edNameSchool = (EditText) findViewById(R.id.edNameSchool);
        btSignUp = (Button) findViewById(R.id.btSignUp);
    }

    public void setJsonListCity(String url, int id) {
        new HttpAsyncTask().execute(url);
    }

    public void setJsonListDistrict(String url, String id) {
        String urlDistrict = url + id;
        Log.d(TAG, "url District = " + urlDistrict);
        new HttpAsyncTask().execute(urlDistrict);
    }

    public void setJsonListSchool(String url, String id) {
        String urlSchool = url + id;
        Log.d(TAG, " url School = " + urlSchool);
        new HttpAsyncTask().execute(urlSchool);
    }

    public void setHashListCity(JSONArray arrayJson) {
        try {
            hashCity = new HashMap<String, String>();
            if (arrayJson != null) {
                for (int i = 0; i < arrayJson.length(); i++) {
                    hashCity.put(arrayJson.getJSONObject(i).getString("_id"), arrayJson.getJSONObject(i).getString("name"));
                    Log.d("City", " key = " + arrayJson.getJSONObject(i).getString("_id") + " name = " + arrayJson.getJSONObject(i).getString("name"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setHashListDistrict(JSONArray arrayJson) {
        try {
            hashDistrict = new HashMap<String, String>();
            if (arrayJson != null) {
                for (int i = 0; i < arrayJson.length(); i++) {
                    hashDistrict.put(arrayJson.getJSONObject(i).getString("_id"), arrayJson.getJSONObject(i).getString("name"));
                    Log.d("District", " key = " + arrayJson.getJSONObject(i).getString("_id") + " name = " + arrayJson.getJSONObject(i).getString("name"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setHashListSchool(JSONArray arrayJson) {
        try {
            hashSchool = new HashMap<String, String>();
            if (arrayJson != null) {
                for (int i = 0; i < arrayJson.length(); i++) {
                    hashSchool.put(arrayJson.getJSONObject(i).getString("_id"), arrayJson.getJSONObject(i).getString("name"));
                    Log.d("School", " key = " + arrayJson.getJSONObject(i).getString("_id") + " name = " + arrayJson.getJSONObject(i).getString("name"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /*public void showLocatioin() {
        //Create Array Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, location);
        //Find TextView control
        AutoCompleteTextView acTextView = (AutoCompleteTextView) findViewById(R.id.edCity);
        //Set the number of characters the user must type before the drop down list is shown
        acTextView.setThreshold(1);
        //Set the adapter
        acTextView.setAdapter(adapter);
    }*/

}
