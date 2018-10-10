package com.example.dell.lab02_cifrado_estructuradedatosii;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.Guideline;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main2Activity extends AppCompatActivity {

    @BindView(R.id.btnChooseFile)
    ImageButton btnChooseFile;
    @BindView(R.id.btnEncrypt)
    Button btnEncrypt;
    @BindView(R.id.btnDecrypt)
    Button btnDecrypt;
    @BindView(R.id.txtLevel)
    EditText txtLevel;
    @BindView(R.id.txtLabel)
    TextView txtLabel;
    @BindView(R.id.txtContent)
    TextView txtContent;
    @BindView(R.id.guideline)
    Guideline guideline;

    String filename = "";
    int level = 0;
    Trasposicion.ZigZag zigzag = new Trasposicion.ZigZag();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnChooseFile, R.id.btnEncrypt, R.id.btnDecrypt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnChooseFile:
                Intent intent = new Intent()
                        .setType("*/*")
                        .setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
                break;
            case R.id.btnEncrypt:
                try{
                    //Filename with extension .cif
                    String[] split = filename.split("\\.");
                    String nameFile = split[0] + ".cif";

                    try {
                        if(txtLevel.getText().toString() != "")
                        {
                            level = Integer.parseInt(txtLevel.getText().toString());
                            String encodedText = "";
                            //Encrypt message
                            encodedText = zigzag.encrypt(txtContent.getText().toString(), level);
                            txtLevel.setText("");
                            txtContent.setText("");

                            WriteFile(nameFile, encodedText);
                        }
                    } catch(NumberFormatException nfe) {
                        Toast.makeText(this, "Ingresar nivel (número)", Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(this, "No se pudo comprimir.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnDecrypt:
                try{
                    String[] split = filename.split("\\.");
                    String[] split2 = split[0].split("/");
                    System.out.println(split[0]);

                    String nameFile = split2[1] + ".des";
                    try {
                        level = Integer.parseInt(txtLevel.getText().toString());
                    } catch(NumberFormatException nfe) {
                        Toast.makeText(this, "Ingresar nivel (número)", Toast.LENGTH_LONG).show();
                    }

                    String decodedText = "";
                    //Decoded zig zag

                    WriteFile(nameFile, decodedText);
                }
                catch (Exception e) {
                 Toast.makeText(this, "Elija un archivo .cif", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            Uri selectedfile = data.getData();
            filename = selectedfile.getLastPathSegment();

            Toast.makeText(this, selectedfile.getPath(), Toast.LENGTH_LONG).show();
            try {
                Read(selectedfile);
                txtContent.setText(Read(selectedfile));
            } catch (IOException e) {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String Read(Uri uri) throws IOException {
        InputStream input = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder stringBuilder = new StringBuilder();
        String Line;
        while ((Line = reader.readLine()) != null) {
            stringBuilder.append(Line);
        }
        input.close();
        reader.close();
        return stringBuilder.toString();
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
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(content.getBytes());
                fos.close();
                if(txtContent.getText() != "")
                {
                    Toast.makeText(this, "Archivo en: storage/MisCompresiones", Toast.LENGTH_LONG).show();
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
