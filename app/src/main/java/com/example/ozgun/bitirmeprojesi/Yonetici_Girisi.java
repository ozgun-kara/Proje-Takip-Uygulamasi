package com.example.ozgun.bitirmeprojesi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.content.Intent;

public class Yonetici_Girisi extends AppCompatActivity
{

    private EditText txtEmail, txtPassword;
    private FirebaseAuth kimlik_islemi;
    private FirebaseUser mevcut_kullanici;
    private FirebaseDatabase db;
    private String UID;
    private String statu_kontrol;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yonetici__girisi);

        getSupportActionBar().setTitle("Yönetici Girişi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtEmail = (EditText) findViewById(R.id.editText2);
        txtPassword = (EditText) findViewById(R.id.editText3);

        kimlik_islemi = FirebaseAuth.getInstance();
    }

    // GİRİŞ YAP Butonunun çalışması...
    public void Yonetici_Girisi_Yap(View v)
    {
        String Email = txtEmail.getText().toString();
        String Sifre = txtPassword.getText().toString();

        if (TextUtils.isEmpty(Email))
        {
            Toast.makeText(this, "Email boş olamaz", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(Sifre))
        {
            Toast.makeText(this, "Şifre boş olamaz", Toast.LENGTH_SHORT).show();
        }

        else if (Sifre.length() < 6)
        {
            Toast.makeText(this, "Şifre en az 6 haneli olmalı", Toast.LENGTH_SHORT).show();
        }

        else
        {
            kimlik_islemi.signInWithEmailAndPassword(Email, Sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        mevcut_kullanici = kimlik_islemi.getCurrentUser();
                        UID = mevcut_kullanici.getUid();
//                        Toast.makeText(Yonetici_Girisi.this, "UID = " + UID, Toast.LENGTH_SHORT).show();

                        db = FirebaseDatabase.getInstance();    // Database işlemleri için örnek bir " db " isimli nesne yaratıldı.

                        DatabaseReference dbRef = db.getReference("KULLANICILAR/" + UID + "/Bilgileri/");

                        ValueEventListener dinleyici = new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                String a = dataSnapshot.child("Statu").getValue().toString();
                                statu_kontrol = a;
//                                Toast.makeText(Yonetici_Girisi.this, "STATÜ = " + statu_kontrol, Toast.LENGTH_SHORT).show();
                                Log.i("Kontrol90", "1. Aşama Başarılı");

                                if(statu_kontrol.equals("Yonetici"))    //  if (statu_kontrol.equalsIgnoreCase("Yonetici")) kodu da yazılabilir.
                                {
                                    Log.i("Kontrol90", "2. Aşama Başarılı");
                                    Toast.makeText(Yonetici_Girisi.this, "Yönetici Girişi Başarılı", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(Yonetici_Girisi.this, "Hoşgeldin " + mevcut_kullanici.getEmail(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Yonetici_Girisi.this, Yonetici_Paneli.class);
                                    startActivity(intent);
                                }

                                else
                                {
                                    Log.i("Kontrol90", "3. Aşama Başarılı");
                                    Toast.makeText(Yonetici_Girisi.this, "Böyle bir yönetici hesabı bulunamadı!", Toast.LENGTH_SHORT).show();
                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {

                            }
                        };

//                        dbRef.addValueEventListener(dinleyici);   // Veritabanını sürekli dinler.
                        dbRef.addListenerForSingleValueEvent(dinleyici);   // Veritabanını 1 kereliğine dinler.
                    }

                    else
                    {
                        Toast.makeText(Yonetici_Girisi.this, "Giriş Başarısız!", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    // YENİ HESAP OLUŞTUR Butonunun çalışması...
    public void Yeni_Hesap_Olustur(View v)
    {
        Intent intent = new Intent(this, Yonetici_Hesabi_Olustur.class);
        startActivity(intent);
    }
}
