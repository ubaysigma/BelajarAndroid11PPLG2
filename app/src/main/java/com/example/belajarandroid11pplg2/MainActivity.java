package com.example.belajarandroid11pplg2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText EDusername;
    EditText EDpassword;
    Button btnlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        EDpassword = findViewById(R.id.EDpassword);
        EDusername = findViewById(R.id.EDusername);
        btnlogin = findViewById(R.id.btnlogin);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {
        // Tambahkan .trim() untuk membuang spasi gaib yang tidak sengaja terketik
        final String username = EDusername.getText().toString().trim();
        final String password = EDpassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        new AsyncTask<String, Void, String>() {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = new ProgressDialog(MainActivity.this);
                loading.setMessage("Wait...");
                loading.setCancelable(false);
                loading.show();
            }

            @Override
            protected String doInBackground(String... strings) {
                String uname = strings[0];
                String pass = strings[1];

                try {
                    URL url = new URL("https://mediadwi.com/api/latihan/login");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(5000); // Batas waktu tunggu internet
                    conn.setDoOutput(true); // WAJIB ada jika mengirim data pakai POST
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    // PERBAIKAN FORMAT REQUEST: Memakai simbol = dan &
                    String request = "username=" + URLEncoder.encode(uname, "UTF-8") +
                            "&password=" + URLEncoder.encode(pass, "UTF-8");

                    OutputStream os = conn.getOutputStream();
                    os.write(request.getBytes("UTF-8"));
                    os.flush();
                    os.close();

                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                    br.close();
                    return response.toString();

                } catch (Exception e) {
                    e.printStackTrace();
                    // JANGAN taruh Toast di sini. Kembalikan nilai null jika error.
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (loading != null && loading.isShowing()) {
                    loading.dismiss();
                }

                // Jika result null (masuk blok catch di doInBackground), tampilkan error di sini
                if (result == null) {
                    Toast.makeText(MainActivity.this, "Gagal terhubung ke server / Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean status = jsonObject.getBoolean("status");
                    String message = jsonObject.getString("message");

                    if (status) {
                        String token = jsonObject.getString("token");
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,HomePage.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error parsing: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(username, password);
    }
}