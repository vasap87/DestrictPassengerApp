package ru.alexandrkotovfrombutovo.destrictpassengerapp.fragments;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.springframework.http.ResponseEntity;

import java.util.concurrent.ExecutionException;

import ru.alexandrkotovfrombutovo.destrictpassengerapp.R;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.UserInfo;
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
        View view = inflater.inflate(R.layout.registration_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mPhoneNumberEditText = view.findViewById(R.id.phoneEditText);
        mPhoneNumberEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher(getResources().getString(R.string.country_code)));
        mConfirmBtn = view.findViewById(R.id.confirmPhoneButton);
        mConfirmBtn.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            UserInfo userInfo = new UserInfo();
            userInfo.setUuid(mUserUuid);
            userInfo.setPhone(String.valueOf(mPhoneNumberEditText.getText()));
            PostUserInfoTask task = new PostUserInfoTask(getActivity());
            task.execute(userInfo);
            try {
                ResponseEntity<UserInfo> entityUserInfo = task.get();
                if(entityUserInfo!=null){
                    FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
                    RouteListFragment fragment = new RouteListFragment();
                    fragment.setUserInfo(userInfo);
                    transaction.replace(R.id.content_frame, fragment);
                    transaction.commit();
                } else {
                    Toast.makeText(getActivity(), "connection timeout", Toast.LENGTH_SHORT).show();
                }
            } catch (InterruptedException e) {
                Log.i(TAG, e.getMessage());
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        });
    }
}
