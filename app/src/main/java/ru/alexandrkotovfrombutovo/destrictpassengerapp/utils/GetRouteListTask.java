package ru.alexandrkotovfrombutovo.destrictpassengerapp.utils;

import android.os.AsyncTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.Route;

/**
 * Created by alexkotov on 16.01.18.
 */

public class GetRouteListTask extends AsyncTask<Void,Void,ResponseEntity<Route[]>> {

    @Override
    protected ResponseEntity<Route[]> doInBackground(Void... voids) {
        ResponseEntity<Route[]> responseEntity;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        try {
             responseEntity = restTemplate.getForEntity("http://172.31.11.110:8080/routes/all", Route[].class);
        }
        catch (Exception e){
            return null;
        }
        return responseEntity;
    }
}
