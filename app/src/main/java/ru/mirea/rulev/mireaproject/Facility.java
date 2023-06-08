package ru.mirea.rulev.mireaproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.map.TextStyle;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;



import ru.mirea.rulev.mireaproject.databinding.FragmentFacilityBinding;



public class Facility extends Fragment implements UserLocationObjectListener  {

    private FragmentFacilityBinding binding;
    private MapView mapView;
    private static final int REQUEST_CODE_PERMISSION = 200;
    private UserLocationLayer userLocationLayer;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFacilityBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        MapKitFactory.initialize(root.getContext());

        int cOARSE_LOCATION = ContextCompat.checkSelfPermission(root.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int fINE_LOCATION = ContextCompat.checkSelfPermission(root.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);

        mapView = binding.mapview;
        mapView.getMap().move(new CameraPosition(new Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f), new Animation(Animation.Type.SMOOTH, 0), null);

        if (cOARSE_LOCATION == PackageManager.PERMISSION_GRANTED || fINE_LOCATION == PackageManager.PERMISSION_GRANTED) {
            MapKit mapKit = MapKitFactory.getInstance();
            mapKit.resetLocationManagerToDefault();
            userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
            userLocationLayer.setVisible(true);
            userLocationLayer.setHeadingEnabled(true);
            userLocationLayer.setObjectListener(this);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[] {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);
        }
        binding.imageButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.getMap().move(new CameraPosition(mapView.getMap().getCameraPosition().getTarget(),
                                mapView.getMap().getCameraPosition().getZoom()+1, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 1),
                        null);
            }
        });
        binding.imageButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.getMap().move(new CameraPosition(mapView.getMap().getCameraPosition().getTarget(),
                                mapView.getMap().getCameraPosition().getZoom()-1, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 1),
                        null);
            }
        });
        binding.imageButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userLocationLayer != null && userLocationLayer.cameraPosition() != null) {
                    mapView.getMap().move(
                            new CameraPosition(userLocationLayer.cameraPosition().getTarget(), 15.0f, 0.0f, 0.0f),
                            new Animation(Animation.Type.SMOOTH, 1),
                            null);
                }
            }
        });

        PlacemarkMapObject placemark1 = mapView.getMap().getMapObjects().addPlacemark(new Point(55.769717, 37.407492));
        placemark1.setText("Лучшая Шаурмечная");
        TextStyle textStyle = new TextStyle();
        textStyle.setColor(Color.RED);
        textStyle.setSize(16);
        placemark1.setTextStyle(textStyle);
        placemark1.setOpacity(0.5f);
       // placemark.setIcon(ImageProvider.fromResource(root.getContext(), R.drawable.baseline_pin_drop_24));
        placemark1.addTapListener(new MapObjectTapListener() {
            @Override
            public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point
                    point) {
                Toast.makeText(root.getContext(),"Крылатская улица, 33к3\nЛучшая шаурмечная у меня на районе", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        PlacemarkMapObject placemark2 = mapView.getMap().getMapObjects().addPlacemark(new Point(55.652404, 37.498800));
        placemark2.setText("РУДН");
        placemark2.setTextStyle(textStyle);
        placemark2.setOpacity(0.5f);
        // placemark.setIcon(ImageProvider.fromResource(root.getContext(), R.drawable.baseline_pin_drop_24));
        placemark2.addTapListener(new MapObjectTapListener() {
            @Override
            public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point
                    point) {
                Toast.makeText(root.getContext(),"ул. Миклухо-Маклая, 6\nХудший универ в москве", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        PlacemarkMapObject placemark3 = mapView.getMap().getMapObjects().addPlacemark(new Point(55.794053, 37.701741));
        placemark3.setText("Любимая Стромынка");
        placemark3.setTextStyle(textStyle);
        placemark3.setOpacity(0.5f);
        // placemark.setIcon(ImageProvider.fromResource(root.getContext(), R.drawable.baseline_pin_drop_24));
        placemark3.addTapListener(new MapObjectTapListener() {
            @Override
            public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point
                    point) {
                Toast.makeText(root.getContext(),"ул. Стромынка, 20\nЛюбимое место", Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        return root;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            //isWork = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();

    }

    @Override
    public void onObjectAdded(@NonNull UserLocationView userLocationView) {
        userLocationLayer.setAnchor(
                new PointF((float)(mapView.getWidth() * 0.5),
                        (float)(mapView.getHeight() * 0.5)),
                new PointF((float)(mapView.getWidth() * 0.5),
                        (float)(mapView.getHeight() * 0.83)));

        userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                getContext(), android.R.drawable.arrow_up_float));


        CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();
        pinIcon.setIcon(
                "pin",
                ImageProvider.fromResource(getContext(), R.drawable.baseline_person_pin_circle_24),
                new IconStyle().setAnchor(new PointF(0.5f, 0.5f))
                        .setRotationType(RotationType.ROTATE)

                        .setZIndex(1f)
                        .setScale(0.5f)

        );

        userLocationView.getAccuracyCircle().setFillColor(Color.BLUE & 0x99ffffff);
    }

    @Override
    public void onObjectRemoved(@NonNull UserLocationView userLocationView) {

    }

    @Override
    public void onObjectUpdated(@NonNull UserLocationView userLocationView, @NonNull ObjectEvent objectEvent) {

    }
}