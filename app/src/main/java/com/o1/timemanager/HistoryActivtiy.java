package com.o1.timemanager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistoryActivtiy extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://121.36.56.36:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("apicode", 12);
        body.addProperty("userAcnt", "Lyh");
        api.post(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                if (response.body() == null) {
                    return;
                }
                //解析返回的JSONs
                JsonArray jsondata = response.body().get("actList").getAsJsonArray();
                List<String> dataList = null;
                for (int i = 0 ,size = jsondata.size();i<size;i++){
                    dataList.add(jsondata.get(i).toString());
                }
                //适配器
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        HistoryActivtiy.this,
                        android.R.layout.simple_list_item_1,
                        dataList
                );
                ListView listHistory = findViewById(R.id.listViewHistory);
                listHistory.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                Toast.makeText(HistoryActivtiy.this, "刷新失败！", Toast.LENGTH_SHORT).show();
            }
        });





    }
}
