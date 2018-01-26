package ru.alexandrkotovfrombutovo.destrictpassengerapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.alexandrkotovfrombutovo.destrictpassengerapp.R;
import ru.alexandrkotovfrombutovo.destrictpassengerapp.models.Route;

/**
 * Created by alexkotov on 15.01.18.
 */

public class RouteAdapter extends ArrayAdapter<Route> implements Filterable {
    private Context mContext;
    private RouteFilter mRouteFilter;
    private List<Route> mRoutes;
    public static final String TAG = "RouteAdapter";

    public RouteAdapter(@NonNull Context context) {
        super(context, R.layout.route_item);
        this.mContext = context;
    }

    public void setData(List<Route> data){
        clear();
        if(data!=null){
            mRoutes = data;
            addAll(data);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.i(TAG, "getView, position = " + position);
        Route route = getItem(position);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.route_item, parent, false);

        TextView fromText =  row.findViewById(R.id.fromTextView);
        TextView toText = ( row.findViewById(R.id.toTextView));
        TextView timeToStart =  row.findViewById(R.id.time_to_start);
        ImageView image =  row.findViewById(R.id.imageRoute);
        fromText.setText(route.getFromRoute()==null ? " " : route.getFromRoute());
        toText.setText(route.getToRoute()==null ? " " : route.getToRoute());
        timeToStart.setText(getTimeToStart(route.getStartDateTime()));
        if (route.getDriver()) {
            image.setImageResource(R.drawable.ic_drive_eta);
        } else {
            image.setImageResource(R.drawable.ic_directions_run);
        }
        return row;
    }

    @NonNull
    private CharSequence getTimeToStart(Long startDateTime) {
        Log.i(TAG,"Method: getTimeToStart, param startDateTime = "+ startDateTime);
        String minuteLetter = mContext.getResources().getString(R.string.minuteLeter);
        Long currentTimeMillis = System.currentTimeMillis();
        Log.i(TAG,"Method: getTimeToStart, currentTimeMillis = "+ currentTimeMillis);
        long timeToStart = startDateTime-currentTimeMillis;
        Log.i(TAG,"Method: getTimeToStart, timeToStart = "+ timeToStart);
        if(timeToStart<0L) {
            return "0 "+ minuteLetter;
        }
        long minutes = timeToStart / (1000 * 60);
        return minutes + " " + minuteLetter;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if(mRouteFilter ==null){
            mRouteFilter = new RouteFilter();
        }
        return mRouteFilter;
    }

    private class RouteFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Route> filteredRoutes = new ArrayList<>();
            if (constraint.toString().equals("mine")){

            }else {
                long currenTimeMillis = System.currentTimeMillis();
                for (Route route : mRoutes) {
                    if((route.getStartDateTime()-currenTimeMillis)/(1000*60) < -5){
                        filteredRoutes.add(route);
                    }
                }
            }
            results.count = filteredRoutes.size();
            results.values = filteredRoutes;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            setData((List<Route>) results.values);
        }
    }
}
