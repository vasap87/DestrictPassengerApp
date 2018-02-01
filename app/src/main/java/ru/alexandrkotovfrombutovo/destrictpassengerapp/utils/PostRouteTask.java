package ru.alexandrkotovfrombutovo.destrictpassengerapp.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.Route;

/**
 * Created by alexkotov on 02.01.18.
 */

public class PostRouteTask extends AsyncTask<Route, Void, ResponseEntity<Route>> {

    @SuppressLint("StaticFieldLeak")
    private Context mContext;

    private static final String URL = "http://172.31.11.110:8080/routes/add";

    public PostRouteTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected ResponseEntity<Route> doInBackground(Route... routes) {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        HttpHeaders headers = new HttpHeaders();
        headers.add("duration", String.valueOf(preferences.getInt("minuteToHideRow", 5)));
        headers.add("locations", preferences.getString("location", "0"));
        HttpEntity<Route> entity = new HttpEntity<>(routes[0],headers);
        try {
//                return restTemplate.exchange("http://192.168.1.133:8080/routes", HttpMethod.POST, entity, Route.class);
            return restTemplate.exchange(URL, HttpMethod.POST, entity, Route.class);
        } catch (Exception e) {
            return null;
        }
    }

}


