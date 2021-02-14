package com.example.tripreminder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tripreminder.Dao.firebaseDao.FirebaseUserDao;
import com.example.tripreminder.Database.firebase.DataHolder;
import com.example.tripreminder.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail,edtPassword;
    Button btnLogin;
    TextView txtNewUser;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private SignInButton btnGoogleLogin;
    private GoogleSignInClient googleSignInClient;
    private int SIGN_IN_CODE=1;
    String uName,uEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        initiatizeFields();
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient= GoogleSignIn.getClient(LoginActivity.this,googleSignInOptions);
        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loignWithGoogle();
            }
        });
        txtNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToRegisterActivity();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserToLogen();
            }
        });
    }
    private void AllowUserToLogen() {
        String email=edtEmail.getText().toString();
        String password=edtPassword.getText().toString();
        if (isValid(email,password)){
            loadingBar.setTitle(getString(R.string.signing_in));
            loadingBar.setMessage(getString(R.string.please_wait));
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String currentUserId = mAuth.getCurrentUser().getUid();
                        FirebaseUserDao.getUser(currentUserId, new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                final User databaseUser=(User) snapshot.getValue(User.class);
                                uName=databaseUser.getUserName();
                                uEmail=databaseUser.getEmail();
                                DataHolder.dataBaseUser=databaseUser;
                                DataHolder.authUser=mAuth.getCurrentUser();
                                saveDataInSharedPerefrence();
                                sendToMainActivity();
                                Toast.makeText(LoginActivity.this,""+ R.string.logged_is_successful, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                String message = error.toException().getLocalizedMessage();
                                Toast.makeText(LoginActivity.this, ""+R.string.error + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        });
                    }else{
                        String message = task.getException().toString();
                        Toast.makeText(LoginActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }




    private boolean isValid(String email, String password) {
        boolean valid=true;
        if (TextUtils.isEmpty(email)){
            Toast.makeText(this,getString(R.string.please_enter_email), Toast.LENGTH_SHORT).show();
            valid=false;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this,getString(R.string.please_enter_password), Toast.LENGTH_SHORT).show();
            valid=false;
        }
        return valid;
    }

    private void loignWithGoogle() {
        Intent signInIntent=googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,SIGN_IN_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==SIGN_IN_CODE){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            handleLoginWithGoogleResult(task);
        }
    }

    private void handleLoginWithGoogleResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account=task.getResult(ApiException.class);
            Toast.makeText(LoginActivity.this, getString(R.string.logged_is_successful), Toast.LENGTH_LONG).show();
            firbaseGoogleAuth(account);
        } catch (ApiException e) {
            Toast.makeText(LoginActivity.this, getString(R.string.logged_is_unsuccessful), Toast.LENGTH_LONG).show();
            //  firbaseGoogleAuth(null);
        }
    }

    private void firbaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential authCredential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
        loadingBar.setTitle(getString(R.string.creating_new_account));
        loadingBar.setMessage(getString(R.string.please_wait));
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(LoginActivity.this, "successful", Toast.LENGTH_LONG).show();

                    AllowGoogleAccountToLogen(account);
                }else{
                    
                }
            }
        });
    }

    private void AllowGoogleAccountToLogen(GoogleSignInAccount account) {
        account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account!=null){
            uEmail=account.getEmail();
            uName=account.getDisplayName();
        }
        loadingBar.setTitle(getString(R.string.creating_new_account));
        loadingBar.setMessage(getString(R.string.please_wait));
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        String currentUserId=mAuth.getCurrentUser().getUid();
        User user=new User();
        user.setId(currentUserId);
        user.setEmail(uEmail);
        user.setUserName(uName);
        FirebaseUserDao.addUser(user, currentUserId, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    DataHolder.dataBaseUser=user;
                    DataHolder.authUser=mAuth.getCurrentUser();
                    uName=user.getUserName();
                    uEmail=user.getEmail();
                    saveDataInSharedPerefrence();
                    sendToMainActivity();
                    Toast.makeText(LoginActivity.this, ""+R.string.account_created_successfully, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }else{
                    String message = task.getException().toString();
                    Toast.makeText(LoginActivity.this, ""+R.string.error + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });
    }

    private void initiatizeFields() {
        edtEmail=findViewById(R.id.edtEmail);
        edtPassword=findViewById(R.id.edtPassword);
        btnLogin=findViewById(R.id.btnLogin);
        txtNewUser=findViewById(R.id.txtNewUser);
        btnGoogleLogin=findViewById(R.id.btnGoogleLogin);
        loadingBar= new ProgressDialog(LoginActivity.this);
    }

    private void sendToRegisterActivity() {
        Intent intent= new Intent(com.example.tripreminder.LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
    private void sendToMainActivity() {
        Intent intent= new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void saveDataInSharedPerefrence() {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("Name",uName);
        editor.putString("Email",uEmail);
        editor.apply();
    }
}