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
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface ServerService {

    @GET("/index")
    Call<com.example.restclient.Answer> index();
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
    Call<com.example.restclient.User> getOne(@Query("id") int id);

    @FormUrlEncoded
    @POST("user/save")
    Call<Boolean> save(@Field("login") String login, @Field("password") String password);
}



class UserDto {
    public int id;
}

interface HttpService {

    @GET("/index")
    Call<com.example.restclient.UserDto> index();
}


public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.1.37:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    public class RequestToServer extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            com.example.restclient.HttpService httpService = retrofit.create(com.example.restclient.HttpService.class);

            Call<com.example.restclient.UserDto> call = httpService.index();

            try {
                Response<com.example.restclient.UserDto> response = call.execute();
                com.example.restclient.UserDto userDto = response.body();

                runOnUiThread(() -> {
                    TextView textView = findViewById(R.id.answerView);

                    textView.setText(String.valueOf(userDto.id));
                });

            } catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }
    }


    public class Request extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            com.example.restclient.ServerService serverService = retrofit.create(com.example.restclient.ServerService.class);

            Call<com.example.restclient.Answer> call = serverService.index();

            try {
                Response<com.example.restclient.Answer> response = call.execute();
                com.example.restclient.Answer answer = response.body();

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

            com.example.restclient.UserService userService = retrofit.create(com.example.restclient.UserService.class);

            Call<com.example.restclient.User> call = userService.getOne(1);

            try {
                Response<com.example.restclient.User> response = call.execute();
                com.example.restclient.User user = response.body();

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

    public class SaveUser extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            com.example.restclient.UserService userService = retrofit.create(com.example.restclient.UserService.class);

            Call<Boolean> call = userService.save("login", "123");

            try {
                Response<Boolean> response = call.execute();
                Boolean result = response.body();

                runOnUiThread(() -> {
                    TextView textView = findViewById(R.id.answerView);

                    Toast.makeText(com.example.restclient.MainActivity.this, "New user has saved", Toast.LENGTH_SHORT);
                });

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

        new com.example.restclient.MainActivity.GetUserReq().execute("");

//        new RequestToServer().execute("");

        findViewById(R.id.button).setOnClickListener((view) -> {
            new com.example.restclient.MainActivity.SaveUser().execute("");
        });
    }
}