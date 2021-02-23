package com.example.tripreminder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
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
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText edtUserName,edtEmail,edtPassword;
    Button btnRegister;
    TextView txtHaveAccount;
    private SignInButton btnGoogleLogin;
    private GoogleSignInClient googleSignInClient;
    private int SIGN_IN_CODE=1;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private DatabaseReference RootRef;
    String uName,uEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth= FirebaseAuth.getInstance();
        RootRef= FirebaseDatabase.getInstance().getReference();
        initializeFields();
       /* GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient= GoogleSignIn.getClient(RegisterActivity.this,googleSignInOptions);
        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loignWithGoogle();
            }
        });*/
        txtHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToLoginActiviy();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });
    }

    private void CreateNewAccount() {
        String userName=edtUserName.getText().toString();
        String email=edtEmail.getText().toString();
        String password=edtPassword.getText().toString();
        if (isValid(userName,email,password)){
            loadingBar.setTitle(getString(R.string.creating_new_account));
            loadingBar.setMessage(getString(R.string.please_wait));
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String currentUserID = mAuth.getCurrentUser().getUid();
                        User user=new User();
                        user.setId(currentUserID);
                        user.setUserName(userName);
                        user.setEmail(email);
                        user.setPassword(password);
                        FirebaseUserDao.addUser(user, currentUserID, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    DataHolder.dataBaseUser=user;
                                    DataHolder.authUser=mAuth.getCurrentUser();
                                    uName=user.getUserName();
                                    uEmail=user.getEmail();
                                    saveDataInSharedPerefrence();
                                    sendToMainActivity();
                                    Toast.makeText(RegisterActivity.this, ""+R.string.account_created_successfully, Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }else{
                                    String message = task.getException().toString();
                                    Toast.makeText(RegisterActivity.this, ""+R.string.error + message, Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }
                            }
                        });
                    }else{
                        String message = task.getException().toString();
                        Toast.makeText(RegisterActivity.this, ""+R.string.error + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }


    private boolean isValid(String userName, String email, String password) {
        boolean valid=true;
        if (TextUtils.isEmpty(userName)){
            Toast.makeText(this,getString(R.string.please_enter_user_name), Toast.LENGTH_SHORT).show();
            valid=false;
        }
        if (TextUtils.isEmpty(email)){
            Toast.makeText(this,getString(R.string.please_enter_email), Toast.LENGTH_SHORT).show();
            valid=false;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this,getString(R.string.please_enter_password), Toast.LENGTH_SHORT).show();
            valid=false;
        }
        if (password.length()<6){
            Toast.makeText(this,getString(R.string.password_validation), Toast.LENGTH_SHORT).show();
            valid=false;
        }
        return valid;
    }










    private void initializeFields() {
        edtUserName=findViewById(R.id.edtUserName);
        edtEmail=findViewById(R.id.edtEmail);
        edtPassword=findViewById(R.id.edtPassword);
        btnRegister=findViewById(R.id.btnRegister);
        txtHaveAccount=findViewById(R.id.txtHaveAccount);
        //btnGoogleLogin=findViewById(R.id.btnGoogleLogin);
        loadingBar= new ProgressDialog(RegisterActivity.this);
    }

    private void sendToLoginActiviy() {
        Intent intent= new Intent(com.example.tripreminder.RegisterActivity.this, com.example.tripreminder.LoginActivity.class);
        startActivity(intent);
    }
    private void sendToMainActivity() {
        Intent intent= new Intent(RegisterActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void saveDataInSharedPerefrence() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("Name",uName);
        editor.putString("Email",uEmail);
        editor.apply();
    }
}