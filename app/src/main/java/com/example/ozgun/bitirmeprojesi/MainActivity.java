package com.example.ozgun.bitirmeprojesi;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // YÖNETİCİ GİRİŞİ Butonunun çalışması...
    public void Yonetici_Girisi(View v)
    {
        Intent intent = new Intent(this, Yonetici_Girisi.class);
        startActivity(intent);
    }

    // ÇALIŞAN GİRİŞİ Butonunun çalışması...
    public void Calisan_Girisi(View v)
    {
        Intent intent = new Intent(this, Calisan_Girisi.class);
        startActivity(intent);
    }

//    // ÇIKIŞ Butonunun çalışması...
//    public void Uygulamadan_Cikis_Yap(View v)
//    {
//        finish();
//        System.exit(0);
//    }

}
