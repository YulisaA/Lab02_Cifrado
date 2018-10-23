package com.example.dell.lab02_cifrado_estructuradedatosii;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import RSAEncrypt.RSA;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityRSAKey extends AppCompatActivity {


    @BindView(R.id.btnEncrypt)
    Button btnEncrypt;
    @BindView(R.id.btnGenerateKeys)
    Button btnGenerateKeys;
    @BindView(R.id.txtPValue)
    EditText txtPValue;
    @BindView(R.id.txtQValue)
    EditText txtQValue;
    @BindView(R.id.txtLabel)
    TextView txtLabel;
    RSA rsa = new RSA();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsakey);
        ButterKnife.bind(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick({R.id.btnEncrypt, R.id.btnGenerateKeys})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnEncrypt:
                Intent intent = new Intent(this, ActivityRSA.class);
                startActivity(intent);
                break;
            case R.id.btnGenerateKeys:
                    try {
                        if(txtPValue.getText().toString() != "" && txtQValue.getText().toString() != "")
                        {
                            String filePublicKey = "public.key";
                            String filePrivateKey = "private.key";
                            //Generar llaves
                            String[] keys = rsa.generateKeys(txtPValue.getText().toString(), txtQValue.getText().toString());
                            txtPValue.setText("");
                            txtQValue.setText("");
                            WriteFile(filePublicKey, keys[0]);
                            WriteFile(filePrivateKey, keys[1]);
                        }
                    } catch(NumberFormatException nfe) {
                        Toast.makeText(this, "Ingresar valor de p y q.", Toast.LENGTH_LONG).show();
                    }
                break;
        }
    }

    private Boolean isExternalStorage() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.i("State", "writable");
            return true;
        } else {
            return false;
        }
    }

    public void WriteFile(String filename, String content) {
        if (isExternalStorage() && verifyPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            boolean exist = true;
            if (exist) {
                File newFile;
                newFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/MisCompresiones");
                newFile.mkdirs();
            }
            File f;
            f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MisCompresiones", filename);


            try {
                if (filename.length() != 0) {
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(content.getBytes());
                    fos.close();
                    Toast.makeText(this, "Llaves en: storage/MisCompresiones", Toast.LENGTH_LONG).show();

                }
            } catch (IOException e) {
                e.printStackTrace();

            }

        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }
    }

    public Boolean verifyPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        if (check == PackageManager.PERMISSION_GRANTED) {
            Log.i("w", "permisiongranted");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        return (check == PackageManager.PERMISSION_GRANTED);

    }
}
