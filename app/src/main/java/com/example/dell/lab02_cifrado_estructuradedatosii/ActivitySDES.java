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
import java.io.OutputStreamWriter;

import SDESAlgorithm.SDES;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivitySDES extends AppCompatActivity {

    @BindView(R.id.btnChooseFile)
    ImageButton btnChooseFile;
    @BindView(R.id.btnEncrypt)
    Button btnEncrypt;
    @BindView(R.id.btnDecrypt)
    Button btnDecrypt;
    @BindView(R.id.txtLabel)
    TextView txtLabel;
    @BindView(R.id.txtContent)
    TextView txtContent;
    @BindView(R.id.guideline)
    Guideline guideline;
    @BindView(R.id.txtMasterKey)
    EditText txtMasterKey;

    String filename = "";
    String masterKey = "";
    SDES sdes = new SDES();
    File newFile;
    Uri selectedfile;
    String nameFile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdes);
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
                    //Filename with extension .scif
                    String[] split = filename.split("\\.");
                    nameFile = split[0] + ".scif";

                        if((!txtMasterKey.getText().toString().equals("") && txtMasterKey.getText().length() == 10))
                        {
                            //Encrypt message
                            ReadAndWrite(selectedfile);
                            txtContent.setText("");
                            txtMasterKey.setText("");
                        }
                        else{
                            Toast.makeText(this, "Ingrese llave de 10 bits.", Toast.LENGTH_LONG).show();
                        }
                }
                catch (Exception e){
                    Toast.makeText(this, "No se pudo encriptar.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnDecrypt:
                try {
                    String[] split = filename.split("\\.");
                    String[] split2 = split[0].split("/");
                    System.out.println(split[0]);

                    nameFile = split2[1] + ".txt";
                     if(!txtMasterKey.getText().toString().equals("") && txtMasterKey.getText().length() == 10)
                        {
                            //Decrypt message
                            ReadAndWrite(selectedfile);
                            txtContent.setText("");
                            txtMasterKey.setText("");
                        }
                        else{
                            Toast.makeText(this, "Ingrese llave de 10 bits.", Toast.LENGTH_LONG).show();
                        }
                } catch (Exception e) {
                    Toast.makeText(this, "Elija un archivo .scif", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            selectedfile = data.getData();
            filename = selectedfile.getLastPathSegment();
            try {
                txtContent.setText(ShowFile(selectedfile));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, selectedfile.getPath(), Toast.LENGTH_LONG).show();

        }
    }

    private String ReadAndWrite(Uri uri) throws IOException {
        InputStream input = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
        StringBuilder stringBuilder = new StringBuilder();
        try {
            CreateFile(nameFile);

            FileOutputStream fos = new FileOutputStream(newFile);
            OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
            //ReadMasterKey
            masterKey = txtMasterKey.getText().toString();
            sdes.generateKeys(masterKey);

            int c;
            while ((c = reader.read()) != -1) {
                if (String.valueOf((char) c).equals("\uFEFF")) {

                    continue;
                }
                stringBuilder = stringBuilder.append(String.valueOf((char) c));
                if(!nameFile.contains(".txt"))
                {
                    String result = sdes.encrypt(c);
                    writer.write(result);
                }
                else{
                    String result = sdes.decrypt(c);
                    writer.write(result);
                }
            }

            input.close();
            reader.close();
            writer.flush();
            writer.close();
            Toast.makeText(this, "Archivo en: storage/MisCompresiones", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void CreateFile(String filename) {
        if (isExternalStorage() && verifyPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            boolean exist = true;
            if (exist) {
                File newFile;
                newFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/MisCompresiones");
                newFile.mkdirs();
            }
            newFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MisCompresiones", filename);

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

    private String ShowFile(Uri uri) throws IOException {
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
}
