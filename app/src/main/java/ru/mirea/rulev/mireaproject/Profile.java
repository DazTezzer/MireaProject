package ru.mirea.rulev.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import ru.mirea.rulev.mireaproject.databinding.FragmentProfileBinding;


public class Profile extends Fragment {
    private FragmentProfileBinding binding;
    private Button button = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        button = binding.Save;
        SharedPreferences sharedPref = getContext().getSharedPreferences("mirea_profile_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(!sharedPref.getString("Fname", "unknown").equals("unknown") || !sharedPref.getString("Lname", "unknown").equals("unknown") || !sharedPref.getString("Mname", "unknown").equals("unknown")
                || !sharedPref.getString("Date", "unknown").equals("unknown")|| !sharedPref.getString("Groupe", "unknown").equals("unknown")|| !sharedPref.getString("Number", "unknown").equals("unknown")
                || !sharedPref.getString("Course", "unknown").equals("unknown")|| !sharedPref.getString("Status", "unknown").equals("unknown"))
        {
            binding.FirstNameText.setText(sharedPref.getString("Fname", "unknown"));
            binding.LastNameText.setText(sharedPref.getString("Lname", "unknown"));
            binding.MiddleNameText.setText(sharedPref.getString("Mname", "unknown"));
            binding.DateText.setText(sharedPref.getString("Date", "unknown"));

            binding.GroupeText.setText(sharedPref.getString("Groupe", "unknown"));
            binding.SelfNumberText.setText(sharedPref.getString("Number", "unknown"));
            binding.CourseText.setText(sharedPref.getString("Course", "unknown"));
            binding.StatusText.setText(sharedPref.getString("Status", "unknown"));
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("Fname", binding.FirstNameText.getText().toString());
                editor.putString("Lname", binding.LastNameText.getText().toString());
                editor.putString("Mname", binding.MiddleNameText.getText().toString());
                editor.putString("Date", binding.DateText.getText().toString());

                editor.putString("Groupe", binding.GroupeText.getText().toString());
                editor.putString("Number", binding.SelfNumberText.getText().toString());
                editor.putString("Course", binding.CourseText.getText().toString());
                editor.putString("Status", binding.StatusText.getText().toString());
                editor.apply();
                Toast.makeText(root.getContext(), "Запись завершена", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

}