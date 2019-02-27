package com.projnibbas.ratingapp;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapSpotHandler{
    private static final String CATEGORIES[] = {"Garbage", "Sewage", "Potholes"};
    public List<MapSpot> mapSpots;

    public class MapSpot {
        public double lat;
        public double lon;
        public String category;
        public int rating;

        public int getColor(){
            if (category.equals(CATEGORIES[0]))
                return Color.RED;
            else if(category.equals(CATEGORIES[1]))
                return Color.GREEN;
            else
                return Color.BLUE;
        }
    }

    public MapSpotHandler(String json){
        try{
            Log.d("Vallabh", json);
            JSONArray ratings = new JSONObject(json).getJSONArray("ratings");
            mapSpots = new ArrayList<MapSpot>();
            for(int i = 0; i < ratings.length(); i++){
                JSONObject obj = ratings.getJSONObject(i);
                String category = obj.getString("category");
                int rating = obj.getInt("rating");
                JSONArray coords = obj.getJSONArray("coords_list");
                for(int j = 0; j < coords.length(); j++){
                    MapSpot mapSpot = new MapSpot();
                    mapSpot.lat = coords.getJSONArray(j).getInt(0);
                    mapSpot.lon = coords.getJSONArray(j).getInt(1);
                    mapSpot.category = category;
                    mapSpot.rating = rating;
                    mapSpots.add(mapSpot);
                }
            }
        }
        catch (JSONException e){
            Log.e("Vallabh", "JSON cup", e);
        }

    }

    public void setMarkersOnMap(GoogleMap map){
        for(MapSpot ms : mapSpots){
            Circle circle = map.addCircle(new CircleOptions()
                    .center(new LatLng(ms.lat, ms.lon))
                    .radius(100 * (5-ms.rating))
                    .strokeColor(ms.getColor())
                    .fillColor(ms.getColor()));
        }
    }


}
