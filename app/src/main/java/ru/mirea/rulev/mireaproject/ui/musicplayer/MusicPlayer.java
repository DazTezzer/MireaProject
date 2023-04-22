package ru.mirea.rulev.mireaproject.ui.musicplayer;

import static android.Manifest.permission.FOREGROUND_SERVICE;
import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.mirea.rulev.mireaproject.PlayerService;
import ru.mirea.rulev.mireaproject.databinding.FragmentMusicPlayerBinding;

public class MusicPlayer extends Fragment {
    private int PermissionCode = 200;
    private FragmentMusicPlayerBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMusicPlayerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        if (ContextCompat.checkSelfPermission(root.getContext(), POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{POST_NOTIFICATIONS, FOREGROUND_SERVICE}, PermissionCode);
        }
        binding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(getContext(), PlayerService.class);
                ContextCompat.startForegroundService(root.getContext(), serviceIntent);
            }
        });
        binding.imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                root.getContext().stopService(
                        new Intent(root.getContext(), PlayerService.class));
            }
        });
        return root;
    }

}