package ru.mirea.rulev.mireaproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import ru.mirea.rulev.mireaproject.databinding.FragmentNetBinding;

public class Net extends Fragment {
    private FragmentNetBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNetBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        Document doc = null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://svo-map.ru/").get();
                    Element text = doc.select("div.post-excerpt").first();
                    String newstext = text.text();
                    text = doc.select("div.post-date").first();
                    String newstitle = text.text();
                    text = doc.select("div.post-title").first();
                    String newnews = text.text();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.textView10.setText(newnews+ "\n\n" +newstitle  + "\n" + newstext);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return root;
    }
}