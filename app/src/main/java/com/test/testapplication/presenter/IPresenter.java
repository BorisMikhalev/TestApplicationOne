package com.test.testapplication.presenter;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public interface IPresenter {
    void showMap(ArrayList<GeoPoint> points);
    void showErrorMessage();
}
