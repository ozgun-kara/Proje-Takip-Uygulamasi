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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.content.Intent;
import java.util.ArrayList;

public class Yeni_Proje_Olustur extends AppCompatActivity
{
    private EditText txtProjeAdi;
    private FirebaseAuth kimlik_islemi;
    private FirebaseUser mevcut_kullanici;
    private FirebaseDatabase db;
    private String Email,Sifre,Statu,UID,Kullanici_Adi;

    private ArrayList<dbProjeler> Projeler_Listesi = new ArrayList<dbProjeler> ();
    private ArrayList<String> Proje_Isimleri_Listesi = new ArrayList<String> ();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yeni__proje__olustur);

        getSupportActionBar().setTitle("Yeni Proje Oluştur");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);    //  Bu aktivite için üstteki geri tuşu çalışmıyor.

        txtProjeAdi = (EditText) findViewById(R.id.editText20);

        kimlik_islemi = FirebaseAuth.getInstance();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        kimlik_islemi = FirebaseAuth.getInstance();
        mevcut_kullanici = kimlik_islemi.getCurrentUser();
        Email = mevcut_kullanici.getEmail();    // Authentication'dan veri çekildi.
        UID = mevcut_kullanici.getUid();        // Authentication'dan veri çekildi.

        db = FirebaseDatabase.getInstance();    // Uygulama database'e bağlandı.
    }

    // PROJE OLUŞTUR Butonunun çalışması...
    public void Proje_Olustur(View v)
    {
        final String Proje_Adi = txtProjeAdi.getText().toString();

        if (TextUtils.isEmpty(Proje_Adi))
        {
            Toast.makeText(this, "Proje Adı boş olamaz", Toast.LENGTH_SHORT).show();
        }

        else
        {
            mevcut_kullanici = kimlik_islemi.getCurrentUser();
            String UID = mevcut_kullanici.getUid();
            db = FirebaseDatabase.getInstance();    // Database işlemleri örnek bir " db " isimli nesne yaratıldı.

            Date tarih = new Date();
            SimpleDateFormat bugun = new SimpleDateFormat("yyyy/MM/dd");

            // DB YAZMA AŞAMASI
            DatabaseReference dbRef = db.getReference("KULLANICILAR/" + UID + "/Projeleri/");
            String Proje_ID = dbRef.push().getKey();
            DatabaseReference dbRef2 = db.getReference("KULLANICILAR/" + UID + "/Projeleri/" + Proje_ID + "/");

            dbRef2.child("Proje_Adi").setValue(Proje_Adi);
            dbRef2.child("Proje_Baslangic_Tarihi").setValue(bugun.format(tarih));
            dbRef2.child("Proje_ID").setValue(Proje_ID).addOnSuccessListener(new OnSuccessListener<Void>()
            {
                @Override
                public void onSuccess(Void aVoid)
                {
                    Log.i("Bilgi", "Veritabanına Yazma İşlemleri Başarılı");
                    Toast.makeText(Yeni_Proje_Olustur.this, Proje_Adi + " isimli proje başarıyla oluşturuldu", Toast.LENGTH_SHORT).show();

                    Projeler_Datatabase_Okuma_Islemleri();

                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {

                }
            });
        }
    }

    public void Projeler_Datatabase_Okuma_Islemleri()
    {
        Projeler_Listesi.clear();
        Proje_Isimleri_Listesi.clear();

        DatabaseReference dbRef = db.getReference("KULLANICILAR/" + UID + "/Projeleri/");

        ValueEventListener dinleyici = new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                long cnt = dataSnapshot.getChildrenCount();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    dbProjeler tempProject = new dbProjeler();
                    tempProject.Proje_Adi = postSnapshot.child("Proje_Adi").getValue().toString();
                    tempProject.Proje_Baslangic_Tarihi = postSnapshot.child("Proje_Baslangic_Tarihi").getValue().toString();
                    tempProject.Proje_ID = postSnapshot.child("Proje_ID").getValue().toString();
                    Projeler_Listesi.add(tempProject);
                    Proje_Isimleri_Listesi.add(tempProject.Proje_Adi);
                }

                Intent intent = new Intent(Yeni_Proje_Olustur.this, Projeler.class);
                intent.putExtra("Parametre-1", Projeler_Listesi);
                intent.putExtra("Parametre-2", Proje_Isimleri_Listesi);
                startActivity(intent);

                finish();   //  Bu işlem yapılmazsa aktivite geçişlerinde gereksiz tekrarlar yaşanıyor.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        };

        dbRef.addListenerForSingleValueEvent(dinleyici);    // Veritabanını 1 kereliğine dinlettirdik. Sürekli dinletirsek proje oluştururken hata veriyor.
    }

}
