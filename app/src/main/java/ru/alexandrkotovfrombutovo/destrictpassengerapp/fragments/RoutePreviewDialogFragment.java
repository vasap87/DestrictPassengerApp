package ru.alexandrkotovfrombutovo.destrictpassengerapp.fragments;


import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.alexandrkotovfrombutovo.destrictpassengerapp.R;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.Route;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoutePreviewDialogFragment extends DialogFragment {

    private Route mRoute;

    public RoutePreviewDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(6,6,6,6);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ImageView imageView = new ImageView(getActivity());
        imageView.setImageResource(mRoute.getDriver()?R.drawable.ic_drive_eta:R.drawable.ic_directions_run);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        params.setMargins(6,6,6,6);
        imageView.setLayoutParams(params);
        layout.addView(imageView);

        TextView textView = new TextView(getActivity());
        textView.setTextSize(24);
        textView.setGravity(Gravity.CENTER);
        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(R.string.preview_after))
                .append((mRoute.getStartDateTime()-System.currentTimeMillis())/60000)
                .append(getResources().getString(R.string.minuteLeter))
                .append("\n")
                .append(getResources().getString(R.string.from_preview))
                .append(mRoute.getFromRoute())
                .append("\n")
                .append(getResources().getString(R.string.preview_to))
                .append(mRoute.getToRoute());
        textView.setText(sb.toString());
        layout.addView(textView);


        ImageButton imageButtonCall = new ImageButton(getActivity());
        imageButtonCall.setLayoutParams(params);
        imageButtonCall.setImageResource(android.R.drawable.ic_menu_call);
        imageButtonCall.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:"+ mRoute.getUser().getPhone()));
            startActivity(callIntent);
        });

        layout.addView(imageButtonCall);

        ImageButton imageButtonSend = new ImageButton(getActivity());
        imageButtonSend.setLayoutParams(params);
        imageButtonSend.setImageResource(android.R.drawable.ic_dialog_email);
        imageButtonSend.setOnClickListener(v -> {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone="
                    + preparePhoneNum(mRoute.getUser().getPhone())));
            startActivity(sendIntent);
        });
        layout.addView(imageButtonSend);

        return layout;
    }

    public void setRoute(Route route) {
        this.mRoute = route;
    }

    private String preparePhoneNum(String noParse){
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : noParse.toCharArray()) {
            if(Character.isDigit(c)) stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }
}
