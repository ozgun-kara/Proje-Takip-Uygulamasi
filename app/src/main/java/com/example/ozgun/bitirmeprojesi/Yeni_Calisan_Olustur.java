package com.example.ozgun.bitirmeprojesi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Yeni_Calisan_Olustur extends AppCompatActivity
{

    private EditText txtUserName, txtEmail, txtPassword;
    private FirebaseAuth kimlik_islemi;
    private FirebaseUser mevcut_kullanici;
    private FirebaseDatabase db;

    private String Yonetici_UID, yonetici_maili, yonetici_sifresi;

    private String statu_kontrol;   // DÜZENLENECEK!!!

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yeni__calisan__olustur);

        getSupportActionBar().setTitle("Çalışan Hesabı Oluştur");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);    //  Bu aktivite için üstteki geri tuşu çalışmıyor.

        txtEmail = (EditText) findViewById(R.id.editText31);
        txtPassword = (EditText) findViewById(R.id.editText32);

        // Calisanlar'dan gelen parametrelerin karşılanması
        Bundle extras = getIntent().getExtras();
        Yonetici_UID = extras.getString("Parametre-1");
        yonetici_maili = extras.getString("Parametre-2");
        yonetici_sifresi = extras.getString("Parametre-3");

        kimlik_islemi = FirebaseAuth.getInstance();
    }

    // ÇALIŞAN HESABI OLUŞTUR Butonunun çalışması...
    public void Calisan_Hesabı_Olustur(View v)
    {
        final String Email = txtEmail.getText().toString();
        final String Sifre = txtPassword.getText().toString();

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
            kimlik_islemi.createUserWithEmailAndPassword(Email, Sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        mevcut_kullanici = kimlik_islemi.getCurrentUser();
                        String Calisan_UID = mevcut_kullanici.getUid();
                        db = FirebaseDatabase.getInstance();    // Database işlemleri örnek bir " db " isimli nesne yaratıldı.

                        // DB YAZMA AŞAMASI-I
                        DatabaseReference dbRef = db.getReference("KULLANICILAR/" + Calisan_UID + "/Bilgileri/");
                        dbRef.child("Kullanici_Adi").setValue("");  // Yeni çalışanın kullanıcı adı default boş olacak.
                        dbRef.child("Mail").setValue(Email);
                        dbRef.child("Sifre").setValue(Sifre);
                        dbRef.child("Statu").setValue("Calisan");
                        dbRef.child("UID").setValue(Calisan_UID);

                        // DB YAZMA AŞAMASI-II
                        DatabaseReference dbRef2 = db.getReference("KULLANICILAR/" + Yonetici_UID + "/Calisanlari/" + Calisan_UID + "/" );
                        dbRef2.child("Kullanici_Adi").setValue("");  // Yeni çalışanın kullanıcı adı default boş olacak.
                        dbRef2.child("Mail").setValue(Email);
                        dbRef2.child("Sifre").setValue(Sifre);
                        dbRef2.child("Statu").setValue("Calisan");
                        dbRef2.child("UID").setValue(Calisan_UID).addOnSuccessListener(new OnSuccessListener<Void>()
                        {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Log.i("Bilgi", "Veritabanına Yazma İşlemleri Başarılı");
                                Toast.makeText(Yeni_Calisan_Olustur.this,"Çalışan hesabı başarıyla oluşturuldu.", Toast.LENGTH_SHORT).show();

                                Log.i("Kontrol33", mevcut_kullanici.getEmail());

                                kimlik_islemi.signOut();

                                // Yöneticinin tekrar sistemde online görünme işlemleri...
                                kimlik_islemi.signInWithEmailAndPassword(yonetici_maili, yonetici_sifresi).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task)
                                    {
                                        if (task.isSuccessful())
                                        {
                                            mevcut_kullanici = kimlik_islemi.getCurrentUser();
                                        }

                                        else
                                        {

                                        }

                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {

                            }
                        });
                    }

                    else
                    {
                        Toast.makeText(Yeni_Calisan_Olustur.this, "Bir hata oluştu!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }
}
