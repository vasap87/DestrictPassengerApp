package ru.alexandrkotovfrombutovo.destrictpassengerapp.utils;


import android.os.AsyncTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.Route;

/**
 * Created by alexkotov on 02.01.18.
 */

public class PostRouteTask extends AsyncTask<Route, Void, ResponseEntity<Route>> {


    @Override
    protected ResponseEntity<Route> doInBackground(Route... routes) {

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            HttpEntity<Route> entity = new HttpEntity<>(routes[0]);
            try {
                return restTemplate.exchange("http://192.168.122.1:8080/routes", HttpMethod.POST, entity, Route.class);
            }
            catch (Exception e){
                return null;
            }
    }

}


