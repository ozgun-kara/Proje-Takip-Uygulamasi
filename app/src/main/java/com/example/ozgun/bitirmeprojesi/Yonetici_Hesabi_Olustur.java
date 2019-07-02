package com.example.ozgun.bitirmeprojesi;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Yonetici_Hesabi_Olustur extends AppCompatActivity
{

    private EditText txtUserName, txtEmail, txtPassword;
    private FirebaseAuth kimlik_islemi;
    private FirebaseUser mevcut_kullanici;
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yonetici__hesabi__olustur);

        getSupportActionBar().setTitle("Yönetici Hesabı Oluştur");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtUserName = (EditText) findViewById(R.id.editText4);
        txtEmail = (EditText) findViewById(R.id.editText5);
        txtPassword = (EditText) findViewById(R.id.editText6);

        kimlik_islemi = FirebaseAuth.getInstance();
    }

    // HESAP OLUŞTUR Butonunun çalışması...
    public void Yonetici_Hesabı_Olustur(View v)
    {
        final String Kullanici_Adi = txtUserName.getText().toString();
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
                        String UID = mevcut_kullanici.getUid();
                        db = FirebaseDatabase.getInstance();    // Database işlemleri örnek bir " db " isimli nesne yaratıldı.

                        // DB YAZMA AŞAMASI
                        DatabaseReference dbRef = db.getReference("KULLANICILAR/" + UID + "/Bilgileri/");
                        dbRef.child("Kullanici_Adi").setValue(Kullanici_Adi);
                        dbRef.child("Mail").setValue(Email);
                        dbRef.child("Sifre").setValue(Sifre);
                        dbRef.child("Statu").setValue("Yonetici");
                        dbRef.child("UID").setValue(UID).addOnSuccessListener(new OnSuccessListener<Void>()
                        {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Log.i("Bilgi", "Veritabanına Yazma İşlemleri Başarılı");
                                Toast.makeText(Yonetici_Hesabi_Olustur.this,"Yönetici hesabı başarıyla oluşturuldu.", Toast.LENGTH_SHORT).show();

                                kimlik_islemi.signOut();
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
                        Toast.makeText(Yonetici_Hesabi_Olustur.this, "Bir hata oluştu!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

}
