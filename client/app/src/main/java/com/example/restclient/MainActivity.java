package com.example.restclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface ServerService {

    @GET("/index")
    Call<Answer> index();
}


class Answer {
    public String data;
}


class User {

    public Integer id;
    public String login;
    public String password;
}

interface UserService {

    @GET("/user/get-one")
    Call<User> getOne(@Query("id") int id);
}


public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.1.37:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    public class Request extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            ServerService serverService = retrofit.create(ServerService.class);

            Call<Answer> call = serverService.index();

            try {
                Response<Answer> response = call.execute();
                Answer answer = response.body();

                runOnUiThread(() -> {
                    TextView textView = findViewById(R.id.answerView);

                    textView.setText(answer.data);
                });

                Log.d("answer", answer.data);
            } catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }
    }

    public class GetUserReq extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            UserService serverService = retrofit.create(UserService.class);

            Call<User> call = serverService.getOne(1);

            try {
                Response<User> response = call.execute();
                User user = response.body();

                runOnUiThread(() -> {
                    TextView textView = findViewById(R.id.answerView);

                    textView.setText(user.login);
                });

                Log.d("user", user.login);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new GetUserReq().execute("");
    }
}