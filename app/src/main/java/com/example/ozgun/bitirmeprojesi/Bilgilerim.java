package com.example.ozgun.bitirmeprojesi;

import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Bilgilerim extends AppCompatActivity
{

    private EditText txtUserName, txtEmail, txtPassword, txtStatu;
    private FirebaseDatabase db;
    private FirebaseAuth kimlik_islemi;
    private FirebaseUser mevcut_kullanici;
    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilgilerim);

        getSupportActionBar().setTitle("Bilgilerim");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtEmail = (EditText) findViewById(R.id.editText7);
        txtPassword = (EditText) findViewById(R.id.editText8);
        txtUserName = (EditText) findViewById(R.id.editText9);
        txtStatu = (EditText) findViewById(R.id.editText10);

        // Yonetici_Paneli'nden gelen parametrelerin karşılanması
        Bundle extras = getIntent().getExtras();
        txtEmail.setText(extras.getString("Parametre-1"));
        txtPassword.setText(extras.getString("Parametre-2"));
        txtUserName.setText(extras.getString("Parametre-3"));
        txtStatu.setText(extras.getString("Parametre-4"));

        kimlik_islemi = FirebaseAuth.getInstance();
        mevcut_kullanici = kimlik_islemi.getCurrentUser();
        UID = mevcut_kullanici.getUid();
        db = FirebaseDatabase.getInstance();    // Uygulama database'e bağlandı.

        Datatabase_Okuma_Islemleri();
    }

    public void Datatabase_Okuma_Islemleri()
    {
        DatabaseReference dbRef = db.getReference("KULLANICILAR/" + UID + "/Bilgileri/");

        ValueEventListener dinleyici = new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String a = dataSnapshot.child("Mail").getValue().toString();
                txtEmail.setText(a);
                String b = dataSnapshot.child("Sifre").getValue().toString();
                txtPassword.setText(b);
                String c = dataSnapshot.child("Kullanici_Adi").getValue().toString();
                txtUserName.setText(c);
                String d = dataSnapshot.child("Statu").getValue().toString();
                txtStatu.setText(d);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        };

//        dbRef.addListenerForSingleValueEvent(dinleyici);
        dbRef.addValueEventListener(dinleyici);     // Veritabanını sürekli dinlettirdik çünkü consolda anlık veri değişimleri uygulamaya yansımalı.
    }

    // BİLGİLERİ GÜNCELLE Butonunun çalışması...

    public void Bilgileri_Guncelle(View v)
    {
        DatabaseReference dbRef = db.getReference("KULLANICILAR/" + UID + "/Bilgileri/");

        if (txtEmail.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Email boş olamaz", Toast.LENGTH_SHORT).show();
        }

        else if (txtPassword.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Şifre boş olamaz", Toast.LENGTH_SHORT).show();
        }

        else if (txtPassword.getText().toString().length() < 6)
        {
            Toast.makeText(this, "Şifre en az 6 haneli olmalı", Toast.LENGTH_SHORT).show();
        }

        else
        {
            // Authentication güncelleniyor

//            mevcut_kullanici.updateEmail(txtEmail.getText().toString());     //  Email ve şifre aynı anda değişemiyor!!!
            mevcut_kullanici.updatePassword(txtPassword.getText().toString());

            // Veritabanı güncelleniyor

            dbRef.child("Sifre").setValue(txtPassword.getText().toString());
            dbRef.child("Kullanici_Adi").setValue(txtUserName.getText().toString());
//            dbRef.child("Mail").setValue(txtEmail.getText().toString());

            Toast.makeText(Bilgilerim.this, "Yönetici bilgileri başarıyla güncellendi", Toast.LENGTH_SHORT).show();
        }
    }

}
