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
    @BindView(R.id.btnSDES)
    Button btnSDES;
    @BindView(R.id.btnRSA)
    Button btnRSA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnZigZag)
    public void onViewClicked() {
        Intent intent = new Intent(this, ActivityZigZag.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnSDES)
    public void onViewClickedSDES() {
        Intent intent = new Intent(this, ActivitySDES.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnRSA)
    public void onViewClickedRSA() {
        Intent intent = new Intent(this, ActivityRSAKey.class);
        startActivity(intent);
    }
}
