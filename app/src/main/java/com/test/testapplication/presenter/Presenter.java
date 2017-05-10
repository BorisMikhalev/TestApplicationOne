package com.test.testapplication.presenter;


import com.test.testapplication.utils.HttpConnection;
import com.test.testapplication.utils.IConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class Presenter {
    private IPresenter iPresenter;
    private ArrayList<GeoPoint> coordinates = new ArrayList<>();

    public Presenter(IPresenter iPresenter) {
        this.iPresenter = iPresenter;
        loadData();
    }

    private void loadData(){
        new HttpConnection("http://test.www.estaxi.ru/route.txt", new IConnection() {
            @Override
            public void result(String s) {
                deserializeData(s);
            }

            @Override
            public void error(String s) {
                iPresenter.showErrorMessage();
            }
        }).execute();

    }

    private void deserializeData(String data) {
        try {
            JSONObject json = new JSONObject(data);
            JSONArray coords = json.getJSONArray("coords");
            for (int i = 0; i < coords.length(); i++){
                coordinates.add(new GeoPoint(coords.getJSONObject(i).getDouble("la"),
                        coords.getJSONObject(i).getDouble("lo")));
            }
            iPresenter.showMap(coordinates);
        }catch (JSONException e){
            e.printStackTrace();
            iPresenter.showErrorMessage();
        }
    }
}
