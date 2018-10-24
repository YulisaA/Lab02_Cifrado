package com.example.dell.lab02_cifrado_estructuradedatosii;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import RSAEncrypt.RSA;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityRSA extends AppCompatActivity {


    String filename = "";
    File newFile;
    Uri selectedfile;
    String nameFile = "";
    Uri selectedKey;
    @BindView(R.id.btnChooseKey)
    ImageButton btnChooseKey;
    @BindView(R.id.btnChooseFile)
    ImageButton btnChooseFile;
    @BindView(R.id.btnEncrypt)
    Button btnEncrypt;
    @BindView(R.id.btnDecrypt)
    Button btnDecrypt;
    @BindView(R.id.txtContent)
    TextView txtContent;
    String key = "";
    RSA rsa = new RSA();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsa);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnChooseKey, R.id.btnChooseFile, R.id.btnEncrypt, R.id.btnDecrypt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnChooseKey:
                Intent intent = new Intent()
                        .setType("*/*")
                        .setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
                break;
            case R.id.btnChooseFile:
                Intent intentFile = new Intent()
                        .setType("*/*")
                        .setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intentFile, "Select a file"), 123);
                break;
            case R.id.btnEncrypt:
                try {
                    //Filename with extension .scif
                    String[] split = filename.split("\\.");
                    nameFile = split[0] + ".rsacif";
                    //Encrypt message
                    ReadAndWrite(selectedfile);
                    txtContent.setText("");

                } catch (Exception e) {
                    Toast.makeText(this, "No se pudo encriptar.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnDecrypt:
                try {
                    String[] split = filename.split("\\.");
                    String[] split2 = split[0].split("/");
                    System.out.println(split[0]);

                    nameFile = split2[1] + ".txt";
                    //Decrypt message
                    String content = ShowFile(selectedfile);
                    String[] splitContent = content.split(",");
                    StringBuilder sb = new StringBuilder();
                    for(int i = 0; i < splitContent.length; i++)
                    {
                        sb.append(rsa.decrypt(Integer.parseInt(splitContent[i]), key));
                    }
                    WriteFile(nameFile, sb.toString());
                    txtContent.setText(sb.toString());
                } catch (Exception e) {
                    Toast.makeText(this, "Elija un archivo .rsacif", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            String name = data.getData().getLastPathSegment();
            if (name.contains(".key")) {
                selectedKey = data.getData();
                try {
                    txtContent.setText(ShowFile(selectedKey));
                    key = txtContent.getText().toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                selectedfile = data.getData();
                filename = selectedfile.getLastPathSegment();
                try {

                    txtContent.setText(ShowFile(selectedfile));
                    Toast.makeText(this, selectedfile.getPath(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

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

            int c;
            while ((c = reader.read()) != -1) {
                if (String.valueOf((char) c).equals("\uFEFF")) {
                    continue;
                }
                stringBuilder = stringBuilder.append(String.valueOf((char) c));
                int result = rsa.encrypt(c, key);
                writer.write(result + ",");
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
                    Toast.makeText(this, "Archivo en: storage/MisCompresiones", Toast.LENGTH_LONG).show();

                }
            } catch (IOException e) {
                e.printStackTrace();

            }

        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }
    }
}
