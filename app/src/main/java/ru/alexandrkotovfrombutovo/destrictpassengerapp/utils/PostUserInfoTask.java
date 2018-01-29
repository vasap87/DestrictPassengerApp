package ru.alexandrkotovfrombutovo.destrictpassengerapp.utils;

import android.os.AsyncTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.UserInfo;

/**
 * Created by vasap87 on 27.01.18.
 */

public class PostUserInfoTask extends AsyncTask<UserInfo, Void, ResponseEntity<UserInfo>> {

//    private static final String URL = "http://192.168.1.133:8080/userservice/add";
    private static final String URL = "http://172.31.11.110:8080/userservice/add";


    @Override
    protected ResponseEntity<UserInfo> doInBackground(UserInfo... userInfos) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        HttpEntity<UserInfo> entity = new HttpEntity<>(userInfos[0]);
        try {
            return restTemplate.exchange(URL, HttpMethod.POST, entity, UserInfo.class);
        } catch (Exception e) {
            return null;
        }
    }
}
