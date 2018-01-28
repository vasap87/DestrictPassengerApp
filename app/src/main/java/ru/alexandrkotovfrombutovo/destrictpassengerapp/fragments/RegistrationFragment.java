package ru.alexandrkotovfrombutovo.destrictpassengerapp.fragments;


import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.ExecutionException;

import ru.alexandrkotovfrombutovo.destrictpassengerapp.activities.MainActivity;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.R;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.UserInfo;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.utils.JsonUtil;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.utils.PostUserInfoTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {

    private String mUserUuid;
    private EditText mPhoneNumberEditText;
    private Button mConfirmBtn;

    private final String TAG = "RegistrationFragment";
    public RegistrationFragment() {
        // Required empty public constructor
    }

    public void setUserUuid(String userUuid) {
        this.mUserUuid = userUuid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_fragment, container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mPhoneNumberEditText = view.findViewById(R.id.phoneEditText);
        mPhoneNumberEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher(getResources().getString(R.string.country_code)));
        mConfirmBtn = view.findViewById(R.id.confirmPhoneButton);
        mConfirmBtn.setOnClickListener(v -> {
            UserInfo userInfo = new UserInfo();
            userInfo.setUuid(mUserUuid);
            userInfo.setPhone(String.valueOf(mPhoneNumberEditText.getText()));
            PostUserInfoTask task = new PostUserInfoTask();
            task.execute(userInfo);
            try {
                ResponseEntity<UserInfo> entityUserInfo = task.get();
                if(entityUserInfo!=null) {
                    userInfo = entityUserInfo.getBody();
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(MainActivity.USER_JSON, JsonUtil.getInstance().serialization(userInfo));
                    editor.putBoolean(MainActivity.IS_REGISTER_TAG, true);
                    editor.apply();

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    RouteListFragment fragment = new RouteListFragment();
                    fragment.setUserInfo(userInfo);
                    transaction.replace(R.id.content_frame, fragment);
                    transaction.commit();
                }else {
                    Toast.makeText(getActivity(), "connection timeout", Toast.LENGTH_SHORT).show();
                }
            } catch (JsonProcessingException e) {
                Log.i(TAG, e.getMessage());
            } catch (InterruptedException e) {
                Log.i(TAG, e.getMessage());
            } catch (ExecutionException e) {
                Log.i(TAG, e.getMessage());
            }

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, new Fragment()).commit();
        });
    }
}
