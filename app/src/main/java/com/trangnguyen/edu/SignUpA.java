package com.trangnguyen.edu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.AbstractCollection;

/**
 * Created by snowflower on 29/12/2015.
 */
public class SignUpA extends Activity {
    Button btNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_a);
        init();
        handlerOnclick();
    }
    public void init(){
        btNext = (Button) findViewById(R.id.btNext);
    }
    public void handlerOnclick() {
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpA.this, SignUpB.class);
                startActivity(intent);
            }
        });
    }
}
