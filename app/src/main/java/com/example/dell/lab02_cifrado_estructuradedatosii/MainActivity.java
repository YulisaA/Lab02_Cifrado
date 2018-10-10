package com.example.dell.lab02_cifrado_estructuradedatosii;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btnZigZag)
    Button btnZigZag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnZigZag)
    public void onViewClicked() {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }
}
