package ru.alexandrkotovfrombutovo.destrictpassengerapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import ru.alexandrkotovfrombutovo.destrictpassengerapp.activities.MainActivity;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.UserInfo;

/**
 * Created by vasap87 on 27.01.18.
 */

public class PostUserInfoTask extends AsyncTask<UserInfo, Void, ResponseEntity<UserInfo>> {

    //    private static final String URL = "http://192.168.1.133:8080/userservice/add";
    private static final String URL = "http://172.31.11.110:8080/userservice/add";

    private Context context;

    public PostUserInfoTask(Context context) {
        this.context = context;
    }

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

    @Override
    protected void onPostExecute(ResponseEntity<UserInfo> userInfoResponseEntity) {
        if (userInfoResponseEntity != null) {
            UserInfo userInfo = userInfoResponseEntity.getBody();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            try {
                editor.putString(MainActivity.USER_JSON, JsonUtil.getInstance().serialization(userInfo));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            editor.putBoolean(MainActivity.IS_REGISTER_TAG, true);
            editor.apply();
        }
    }
}
