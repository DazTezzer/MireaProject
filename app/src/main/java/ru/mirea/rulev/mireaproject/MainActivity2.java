package ru.mirea.rulev.mireaproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import android.os.Bundle;

import ru.mirea.rulev.mireaproject.databinding.ActivityMain2Binding;
import ru.mirea.rulev.mireaproject.databinding.ActivityMainBinding;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMain2Binding binding;
    // START declare_auth
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        String id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        id = "Android id - " + id;
        binding.idandroid.setText(id);

        binding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    signIn(binding.email.getText().toString(), binding.password.getText().toString());
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        binding.signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        binding.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createAccount(binding.email.getText().toString(), binding.password.getText().toString());
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        binding.verifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailVerification();
            }
        });
    }
    private boolean checkForAnyDesk() {
        PackageManager packageManager = getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);

        for (PackageInfo packageInfo : installedPackages) {
            if (packageInfo.packageName.equals("com.anydesk.anydeskandroid")) {
                binding.idandroid.setVisibility(View.INVISIBLE);
                Snackbar.make(findViewById(android.R.id.content), "Обнаруженно приложение anydesk которое может использоваться хакерами для кражи данных", Snackbar.LENGTH_LONG).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },3000);
                //
                return true;
            }
        }
        return false;
    }
    @Override
    public void onStart() {
        super.onStart();
        if(!checkForAnyDesk()){
            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
        }
    }

    // [END on_start_check_user]
    private void updateUI(FirebaseUser user) {
        if (user != null) {


            Intent intent = new Intent(MainActivity2.this, MainActivity.class);
            startActivity(intent);

//            binding.status.setText(getString(R.string.emailpassword_status_fmt, user.getEmail(), user.isEmailVerified()));
//
//            binding.detail.setText(getString(R.string.firebase_status_fmt, user.getUid()));
//
//            binding.create.setVisibility(View.GONE);
//            binding.email.setVisibility(View.GONE);
//            binding.password.setVisibility(View.GONE);
//            binding.signout.setVisibility(View.VISIBLE);
//            binding.signin.setVisibility(View.GONE);
//            binding.verifi.setVisibility(View.VISIBLE);
//            binding.verifi.setEnabled(!user.isEmailVerified());
        } else {
            binding.status.setText("signed_out");
            binding.detail.setText(null);
            binding.create.setVisibility(View.VISIBLE);
            binding.email.setVisibility(View.VISIBLE);
            binding.password.setVisibility(View.VISIBLE);
            binding.signin.setVisibility(View.VISIBLE);
            binding.signout.setVisibility(View.GONE);
            binding.verifi.setVisibility(View.GONE);
        }
    }

    private void createAccount(String email, String password) throws NoSuchAlgorithmException {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        BigInteger bighash = new BigInteger(1, encodedhash);
        String hash_sha = bighash.toString(16);
        Log.d("SHA256-actual", hash_sha.toString());
        Sha256 sha = new Sha256(password);
        mAuth.createUserWithEmailAndPassword(email, sha.ShaHash)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity2.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private boolean validateForm() {
        if(binding.password.getText().toString().length() <6)
        {
            return false;
        }
        return !TextUtils.isEmpty(binding.email.getText().toString()) && android.util.Patterns.EMAIL_ADDRESS.matcher(binding.email.getText().toString()).matches();
    }

    private void signIn(String email, String password) throws NoSuchAlgorithmException {
        Log.d(TAG, "signIn:" + email);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        BigInteger bighash = new BigInteger(1, encodedhash);
        String hash_sha = bighash.toString(16);
        Log.d("SHA256-actual", hash_sha.toString());
        Sha256 sha = new Sha256(password);
        mAuth.signInWithEmailAndPassword(email, sha.ShaHash)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                            Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                            startActivity(intent);
                        } else {

                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            Toast.makeText(MainActivity2.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        if (!task.isSuccessful()) {

                            binding.status.setText("auth_failed");
                        }
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void sendEmailVerification() {
        binding.verifi.setEnabled(false);

        final FirebaseUser user = mAuth.getCurrentUser();
        Objects.requireNonNull(user).sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override

                    public void onComplete(@NonNull Task<Void> task) {

                        binding.verifi.setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity2.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(MainActivity2.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}