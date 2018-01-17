package ru.alexandrkotovfrombutovo.destrictpassengerapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.alexandrkotovfrombutovo.destrictpassengerapp.R;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.Route;

/**
 * Created by alexkotov on 15.01.18.
 */

public class RouteAdapter extends ArrayAdapter<Route> {
    private Context context;

    public RouteAdapter(@NonNull Context context, @NonNull List<Route> objects) {
        super(context, R.layout.route_item, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Route route = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.route_item, parent, false);

        TextView fromText = (TextView) row.findViewById(R.id.fromTextView);
        TextView toText = (TextView) row.findViewById(R.id.toTextView);
        TextView timeToStart = (TextView) row.findViewById(R.id.time_to_start);
        ImageView image = (ImageView) row.findViewById(R.id.imageRoute);
        fromText.setText(route.getFromRoute().isEmpty() ? " " : route.getFromRoute());
        toText.setText(route.getToRoute().isEmpty() ? " " : route.getToRoute());
        timeToStart.setText(route.getStartDate().isEmpty() ? " " : route.getStartDate());
        if (route.getDriver()) {
            image.setImageResource(R.drawable.ic_drive_eta);
        } else {
            image.setImageResource(R.drawable.ic_directions_run);
        }
        return row;
    }
}
