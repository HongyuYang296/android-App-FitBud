package com.example.assignment3.ui.report;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.assignment3.BitmapUtil;
import com.example.assignment3.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import java.io.FileNotFoundException;
import java.nio.ByteBuffer;

public class FacebookActivity extends AppCompatActivity {

    LoginButton loginButton;
    ShareButton sbLink;
    ShareButton sbPhoto;
    CallbackManager callbackManager;
    ImageView imageView;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_facebook);


        loginButton = findViewById(R.id.loginButton);
        imageView = findViewById(R.id.fb_photo);
        sbLink = findViewById(R.id.sb_link);
        sbPhoto = findViewById(R.id.sb_photo);
        callbackManager = CallbackManager.Factory.create();
        //imageView.setImageResource(R.drawable.unnamed);
        shareDialog = new ShareDialog(this);
        Intent intent = getIntent();
        if (intent != null) {
            //show image
            Uri imgUri = Uri.parse(intent.getStringExtra("bitmap"));
            try {
                Bitmap srcBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imgUri));
                if (srcBitmap == null) {
                    Toast.makeText(FacebookActivity.this, "can not load the picture", Toast.LENGTH_SHORT).show();
                    Log.i("TAG", "can not load the picture");
                    finish();
                }
                Bitmap myBitmap = Bitmap.createScaledBitmap(srcBitmap, 500, 900, true);
                imageView.setImageBitmap(myBitmap); //show shoot picture
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(@NonNull FacebookException e) {

            }
        });
        Button back = findViewById(R.id.facebook_back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        //imageView.setImageResource(R.drawable.unnamed);

        ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://www.facebook.com/profile.php?id=100080892737577"))
                .setShareHashtag(new ShareHashtag.Builder()
                        .setHashtag("#fitness").build())
                .build();

        sbLink.setShareContent(shareLinkContent);

        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();


        SharePhoto sharePhoto = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();

        SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                .addPhoto(sharePhoto)
                .build();

        sbPhoto.setShareContent(sharePhotoContent);
    }
}