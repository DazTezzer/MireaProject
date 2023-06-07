package ru.mirea.rulev.mireaproject;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


import ru.mirea.rulev.mireaproject.databinding.FragmentFileInfoBinding;



public class FileInfo extends Fragment {
    private FragmentFileInfoBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFileInfoBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogFragment fragment = new MyDialogFragment();
                fragment.show(getChildFragmentManager(), "mirea");
            }
        });

        return root;
    }
    public void onOpenClicked() {
        if(isExternalStorageReadable())
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    readFileFromExternalStorage();
                }
            }).start();
        }
        else
        {
            Toast.makeText(getContext(),"нет разрешения", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getContext(),"завершено", Toast.LENGTH_SHORT).show();
    }

    public void onSaveClicked() {
        if(isExternalStorageWritable()) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    writeFileToExternalStorage();
                }
            }).start();
        }
        else {
            Toast.makeText(getContext(),"нет разрешения", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getContext(), "завершено", Toast.LENGTH_SHORT).show();
    }

    public void readFileFromExternalStorage() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, String.format("%s.txt",binding.name.getText().toString()));
        try {
            FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();
            String text = "";

            long fileSize = file.length() * 8;
            long fileCreated = file.lastModified();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            String fileCreatedAt = dateFormat.format(fileCreated);
            int lineCount = 0;
            while (line != null) {
                text = text + line;
                line = reader.readLine();
                lineCount ++;
            }
            int count = text.length();
            reader.close();
            String finalText = text;
            int finalLineCount = lineCount;
            int finalCount = count;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.text.setText(finalText);
                    binding.FileSize.setText("Размер файла - " + String.valueOf(fileSize) + "бит");
                    binding.FileDate.setText("Дата создания файла - " + String.valueOf(fileCreatedAt));
                    binding.FileLine.setText("Количество строк - " + String.valueOf(finalLineCount));
                    binding.FileCount.setText("Количество символов - " + String.valueOf(finalCount));

                }
            });
        } catch (Exception e) {
            Log.w("ExternalStorage", String.format("Read from file %s failed", e.getMessage()));
        }
    }

    public void writeFileToExternalStorage() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, String.format("%s.txt",binding.name.getText().toString()));
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsoluteFile());
            OutputStreamWriter output = new OutputStreamWriter(fileOutputStream);



            output.write(binding.text.getText().toString());
            output.close();

        } catch (IOException e) {
            Log.w("ExternalStorage", "Error writing " + file, e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* Проверяем хранилище на доступность чтения и записи*/
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    /* Проверяем внешнее хранилище на доступность чтения */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}