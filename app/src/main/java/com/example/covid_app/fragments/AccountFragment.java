package com.example.covid_app.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.example.covid_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;


public class AccountFragment extends Fragment {

    Context context;
    AppCompatActivity activity;

    private FirebaseAuth firebaseAuth;

    EditText connectionUserName;
    EditText connexionPassword;
    RelativeLayout connectionButtonClick;
    TextView connectionError;
    TextView anonymButtonclick;
    ConstraintLayout createAccount;

    EditText createUserName;
    EditText createPassword1;
    EditText createPassword2;
    RelativeLayout createButtonClick;
    TextView createError;
    TextView createOldUser;
    ConstraintLayout connectAccount;

    LottieAnimationView loadingAnimation;

    ScrollView accountScrollView;
    private Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_account, container, false);

        context = result.getContext();
        activity = (AppCompatActivity) context;

        initView(result);
        OverScrollDecoratorHelper.setUpOverScroll(accountScrollView);
        checkInteractions();
        firebaseAuthentification();

        return result;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    }
    private void checkInteractions() {
        anonymButtonclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
        connectionButtonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connectionUserName.getText().toString().isEmpty() || connexionPassword.getText().toString().isEmpty()){
                    connectionError.setText("Email ou mot de passe invalide");
                    connectionError.setAlpha(1.0f);
                }else {
                    signIn(connectionUserName.getText().toString(), connexionPassword.getText().toString());
                }
            }
        });
    }

    private void createAccount(String mail, String password){
        showLoadingDialog();
        firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideLoadingDialog();
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    assert firebaseUser != null;
                    Toast.makeText(context, "Utilisateur "+ firebaseUser.getEmail() + " enregistre", Toast.LENGTH_SHORT).show();
                    createAccount.setAlpha(0.0f);
                    connectionError.setAlpha(0);
                    connectionUserName.setText(firebaseUser.getEmail());
                    connexionPassword.setText("");
                    connectAccount.setVisibility(View.VISIBLE);
                    connectAccount.setAlpha(1.0f);
                    createAccount.setVisibility(View.GONE);
                }else{
                    createError.setAlpha(1.0f);
                }
            }
        });
    }

    private void signIn(String mail, String password) {
        showLoadingDialog();
        firebaseAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideLoadingDialog();
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                    ft.replace(R.id.fragment, new ConnectedFragment());
                    ft.commit();
                    assert firebaseUser != null;
                    Toast.makeText(context, "Utilisateur "+ firebaseUser.getEmail() + " connecte", Toast.LENGTH_SHORT).show();
                }else {
                    connectionError.setText("Erreur");
                    connectionError.setAlpha(1.0f);
                }
            }
        });
    }

//    void removed(){
//        createOldUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                createAccount.setAlpha(0.0f);
//                connectionError.setAlpha(0);
//                connectAccount.setVisibility(View.VISIBLE);
//                connectAccount.setAlpha(1.0f);
//                createAccount.setVisibility(View.GONE);
//            }
//        });
//        connectionNewUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                connectAccount.setAlpha(0.0f);
//                createError.setAlpha(0);
//                createAccount.setVisibility(View.VISIBLE);
//                createAccount.setAlpha(1.0f);
//                connectAccount.setVisibility(View.GONE);
//            }
//        });
//        createButtonClick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(createUserName.getText().toString().isEmpty() || createPassword1.getText().toString().isEmpty() || createPassword2.getText().toString().isEmpty()){
//                    createError.setText("Email ou mot de passe invalide");
//                    createError.setAlpha(1.0f);
//                }else {
//                    if (!createPassword1.getText().toString().equals(createPassword2.getText().toString())){
//                        createError.setText("Les mots de passe ne correspondent pas");
//                        createError.setAlpha(1.0f);
//                    }else {
//                        createAccount(createUserName.getText().toString(), createPassword1.getText().toString());
//                    }
//                }
//            }
//        });
//    }

    void initView(View view){
        accountScrollView = view.findViewById(R.id.accountScrollView);

        connectionUserName = view.findViewById(R.id.connectionUserName);
        connexionPassword = view.findViewById(R.id.connectionPassword);
        connectionButtonClick = view.findViewById(R.id.connectionButtonClick);
        connectionError = view.findViewById(R.id.connectionError);
        anonymButtonclick = view.findViewById(R.id.anonymButtonclick);
        connectAccount = view.findViewById(R.id.connectAccount);

        createUserName = view.findViewById(R.id.createUserName);
        createPassword1 = view.findViewById(R.id.createPassword1);
        createPassword2 = view.findViewById(R.id.createPassword2);
        createButtonClick = view.findViewById(R.id.createButtonClick);
        createError = view.findViewById(R.id.createError);
        createOldUser = view.findViewById(R.id.oldUser);
        createAccount = view.findViewById(R.id.createAccount);

    }

    void showLoadingDialog(){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_loading_dialog);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LottieAnimationView loadingAnimation = dialog.findViewById(R.id.loadingAnimation);
                loadingAnimation.playAnimation();
                RelativeLayout cancelButtonClick = dialog.findViewById(R.id.cancelButtonClick);
                cancelButtonClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideLoadingDialog();
                        cancelLoading();
                    }
                });
            }
        });
        dialog.show();
    }

    private void cancelLoading() {

    }

    void hideLoadingDialog(){
        if (dialog != null){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LottieAnimationView loadingAnimation = dialog.findViewById(R.id.loadingAnimation);
                    loadingAnimation.pauseAnimation();
                }
            });
            dialog.dismiss();
        }
    }

    void firebaseAuthentification(){
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){
            //
        }else {
            createError.setAlpha(0);
            createError.setVisibility(View.GONE);
            connectionError.setVisibility(View.VISIBLE);
            connectionError.setAlpha(1.0f);
        }
    }
}