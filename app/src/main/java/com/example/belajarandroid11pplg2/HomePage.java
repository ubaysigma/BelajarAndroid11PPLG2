package com.example.belajarandroid11pplg2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    RecyclerView RVuser;
    ArrayList<UserModel> listuser;
    UserAdapter userAdapter;
    Button btnnxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);

        RVuser = findViewById(R.id.RVuser);
        btnnxt = findViewById(R.id.btnnxt);

        listuser = new ArrayList<>();

        userAdapter = new UserAdapter(
                HomePage.this,
                listuser
        );

        RVuser.setLayoutManager(
                new LinearLayoutManager(this)
        );

        RVuser.setAdapter(userAdapter);

        getUser();
    }

    private void getUser() {

        new AsyncTask<Void, Void, String>() {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = new ProgressDialog(HomePage.this);
                loading.setMessage("Loading data user");
                loading.setCancelable(false);
                loading.show();
            }

            @Override
            protected String doInBackground(Void... voids) {

                try {

                    URL url = new URL(
                            "https://jsonplaceholder.typicode.com/users"
                    );

                    HttpURLConnection conn =
                            (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("GET");

                    BufferedReader br =
                            new BufferedReader(
                                    new InputStreamReader(
                                            conn.getInputStream()
                                    )
                            );

                    StringBuilder response =
                            new StringBuilder();

                    String line;

                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }

                    br.close();

                    return response.toString();

                } catch (Exception e) {

                    e.printStackTrace();

                    return null;
                }
            }

            @Override
            protected void onPostExecute(String respon) {

                super.onPostExecute(respon);

                if (loading != null && loading.isShowing()) {
                    loading.dismiss();
                }

                if (respon == null) {
                    return;
                }

                try {

                    JSONArray jsonArrayuser =
                            new JSONArray(respon);

                    for (int i = 0;
                         i < jsonArrayuser.length();
                         i++) {

                        JSONObject jsonObject =
                                jsonArrayuser.getJSONObject(i);

                        UserModel userModel =
                                new UserModel(
                                        jsonObject.getInt("id"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("username"),
                                        jsonObject.getString("email")
                                );

                        listuser.add(userModel);
                    }

                    userAdapter.notifyDataSetChanged();

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

        }.execute();

        btnnxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, MainMenu.class));
            }
        });
    }
}