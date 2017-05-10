package com.test.testapplication;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.test.testapplication.presenter.IPresenter;
import com.test.testapplication.presenter.Presenter;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.PathOverlay;

import java.util.ArrayList;


public class MainActivity extends Activity {
    MapView map;
    IMapController controller;
    TextView infoTxt;
    Button loadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadBtn = (Button) findViewById(R.id.loadBtn);
        infoTxt = (TextView) findViewById(R.id.infoTxt);
        map = (MapView) findViewById(R.id.mapview);
        initMap();
        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });
    }

    private void initMap(){
        map.setTileSource(new XYTileSource(
                "OSMPublicTransport", ResourceProxy.string.mapnik,
                4,
                15,
                256 ,
                ".jpg",
                new String[]{}
        ));
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setTilesScaledToDpi(false);
        controller = map.getController();
        map.setVisibility(View.INVISIBLE);
    }

    private void click(){
        loadBtn.setClickable(false);
        map.setVisibility(View.INVISIBLE);
        infoTxt.setText(R.string.wait_message);
        new Presenter(new IPresenter() {
            @Override
            public void showMap(ArrayList<GeoPoint> points) {
                setupMap(points);
            }

            @Override
            public void showErrorMessage() {
                infoTxt.setText(R.string.error_message);
                loadBtn.setClickable(true);
            }
        });
    }

    private void setupMap(ArrayList<GeoPoint> points){
        loadBtn.setClickable(true);
        infoTxt.setText("");
        map.setVisibility(View.VISIBLE);
        PathOverlay path = new PathOverlay(Color.BLACK, this);
        for(GeoPoint point : points){
            path.addPoints(point);
        }
        controller.setZoom(12);
        GeoPoint center = new GeoPoint(points.get(points.size() / 3).getLatitude(),
                points.get(points.size() - 1).getLongitude());
        controller.setCenter(center);
        map.getOverlays().add(path);
    }
}
