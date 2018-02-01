package ru.alexandrkotovfrombutovo.destrictpassengerapp.utils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.Route;

/**
 * Created by alexkotov on 16.01.18.
 */

public class GetRouteListTask extends AsyncTaskLoader<List<Route>> {

    private List<Route> routes;
    private Context mContext;
    private static final String URL = "http://172.31.11.110:8080/routes/all";

    public GetRouteListTask(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public List<Route> loadInBackground() {
        routes = new ArrayList<>();
        ResponseEntity<Route[]> responseEntity;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        HttpHeaders headers = new HttpHeaders();
        headers.add("duration", String.valueOf(preferences.getInt("minuteToHideRow", 5)));
        headers.add("location", preferences.getString("location", "0"));
        HttpEntity entity = new HttpEntity(headers);
        try {
//            responseEntity = restTemplate.getForEntity("http://192.168.1.133:8080/routes/all", Route[].class);
            responseEntity = restTemplate.exchange(URL, HttpMethod.GET, entity, Route[].class);
            routes = Arrays.asList(responseEntity.getBody());
        }
        catch (Exception e){
            return routes;
        }

        return routes;
    }


    @Override
    public void deliverResult(List<Route> data) {
        if(isReset()&&data!=null){
            onReleaseResources(data);
        }
        List<Route> oldRoutes = data;
        routes = data;

        if(isStarted()){
            super.deliverResult(data);
        }
        if(oldRoutes!=null){
            onReleaseResources(oldRoutes);
        }
    }

    @Override
    protected void onStartLoading() {
        if(routes!=null){
            deliverResult(routes);
        }
        if(takeContentChanged() || routes ==null){
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(List<Route> data) {
        super.onCanceled(data);
        onReleaseResources(data);
    }

    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();

        if(routes!=null){
            onReleaseResources(routes);
            routes=null;
        }
    }

    protected void onReleaseResources(List<Route> routes){}
}
