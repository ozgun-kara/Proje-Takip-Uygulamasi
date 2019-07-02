package com.example.ozgun.bitirmeprojesi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.content.Intent;
import java.util.ArrayList;

public class Yonetici_Paneli extends AppCompatActivity
{

    private FirebaseAuth kimlik_islemi;
    private FirebaseUser mevcut_kullanici;
    private FirebaseDatabase db;
    private String Email,Sifre,Statu,UID,Kullanici_Adi;
    private boolean hosgeldin_flag = false;

    private ArrayList<dbProjeler> Projeler_Listesi = new ArrayList<dbProjeler> ();
    private ArrayList<String> Proje_Isimleri_Listesi = new ArrayList<String> ();

    private ArrayList<dbCalisanlar> Calisanlar_Listesi = new ArrayList<dbCalisanlar> ();
    private ArrayList<String> Calisan_Isimleri_Listesi = new ArrayList<String> ();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yonetici__paneli);

        getSupportActionBar().setTitle("Yönetici Paneli");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        Bilgilerim_Datatabase_Okuma_Islemleri();
        Projeler_Datatabase_Okuma_Islemleri();
//        Calisanlar_Datatabase_Okuma_Islemleri();
    }

    public void Bilgilerim_Datatabase_Okuma_Islemleri()
    {
        DatabaseReference dbRef = db.getReference("KULLANICILAR/" + UID + "/Bilgileri/");

        ValueEventListener dinleyici = new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Email = dataSnapshot.child("Mail").getValue().toString();
                Sifre = dataSnapshot.child("Sifre").getValue().toString();
                Kullanici_Adi = dataSnapshot.child("Kullanici_Adi").getValue().toString();
                Statu = dataSnapshot.child("Statu").getValue().toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        };

        dbRef.addValueEventListener(dinleyici);
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
                Log.d("Kontrol_1", Long.toString(cnt));         // Proje sayısını döndürür
                Log.d("Kontrol_2", dataSnapshot.toString());    // dataSnapshot'daki bütün json verisini döndürür.

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    dbProjeler tempProject = new dbProjeler();
                    tempProject.Proje_Adi = postSnapshot.child("Proje_Adi").getValue().toString();
                    tempProject.Proje_Baslangic_Tarihi = postSnapshot.child("Proje_Baslangic_Tarihi").getValue().toString();
                    tempProject.Proje_ID = postSnapshot.child("Proje_ID").getValue().toString();
                    Projeler_Listesi.add(tempProject);

                    Log.d("Kontrol_3", tempProject.Proje_Adi);
                    Log.d("Kontrol_4", tempProject.Proje_Baslangic_Tarihi);
                    Log.d("Kontrol_5", tempProject.Proje_ID);
                    Log.d("Kontrol_6", "___");

                    Proje_Isimleri_Listesi.add(tempProject.Proje_Adi);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        };

        dbRef.addListenerForSingleValueEvent(dinleyici);    // Veritabanını 1 kereliğine dinlettirdik. Sürekli dinletirsek proje oluştururken hata veriyor.
    }

    public void Calisanlar_Datatabase_Okuma_Islemleri()
    {
        Calisanlar_Listesi.clear();
        Calisan_Isimleri_Listesi.clear();

        DatabaseReference dbRef = db.getReference("KULLANICILAR/" + UID + "/Calisanlari/");

        ValueEventListener dinleyici = new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                long cnt = dataSnapshot.getChildrenCount();
                Log.d("Kontrol_1", Long.toString(cnt));         // Calışan sayısını döndürür
                Log.d("Kontrol_2", dataSnapshot.toString());    // dataSnapshot'daki bütün json verisini döndürür.

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    dbCalisanlar tempCalisan = new dbCalisanlar();
//                    tempCalisan.Calisan_Adi = postSnapshot.child("Calisan_Adi").getValue().toString();
//                    tempCalisan.Calisan_Baslangic_Tarihi = postSnapshot.child("Calisan_Baslangic_Tarihi").getValue().toString();
                    tempCalisan.Calisan_ID = postSnapshot.child("UID").getValue().toString();
                    Calisanlar_Listesi.add(tempCalisan);

                    Log.d("Kontrol_3", tempCalisan.Calisan_Adi);
                    Log.d("Kontrol_4", tempCalisan.Calisan_Baslangic_Tarihi);
                    Log.d("Kontrol_5", tempCalisan.Calisan_ID);
                    Log.d("Kontrol_6", "___");

                    Calisan_Isimleri_Listesi.add(tempCalisan.Calisan_Adi);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        };

        dbRef.addListenerForSingleValueEvent(dinleyici);    // Veritabanını 1 kereliğine dinlettirdik. Sürekli dinletirsek proje oluştururken hata veriyor.
    }

    // PROJELER Butonunun çalışması...
    public void Projeleri_Ac(View v)
    {
        Intent intent = new Intent(this, Projeler.class);
        intent.putExtra("Parametre-1", Projeler_Listesi);
        intent.putExtra("Parametre-2", Proje_Isimleri_Listesi);
        startActivity(intent);
    }

    // ÇALIŞANLAR Butonunun çalışması...
    public void Calisanlari_Ac(View v)
    {
        Intent intent = new Intent(this, Calisanlar.class);
        intent.putExtra("Parametre-1", Calisanlar_Listesi);
        intent.putExtra("Parametre-2", Calisan_Isimleri_Listesi);
        startActivity(intent);
    }

    // BİLGİLERİM Butonunun çalışması...
    public void Bilgilerimi_Ac(View v)
    {
        Intent intent = new Intent(this, Bilgilerim.class);
        intent.putExtra("Parametre-1", Email);
        intent.putExtra("Parametre-2", Sifre);
        intent.putExtra("Parametre-3", Kullanici_Adi);
        intent.putExtra("Parametre-4", Statu);
        startActivity(intent);
    }

    // ÇIKIŞ YAP Butonunun çalışması...
    public void Yonetici_Cikisi_Yap(View v)
    {
        Toast.makeText(Yonetici_Paneli.this, "Çıkış Yaptınız", Toast.LENGTH_SHORT).show();
        kimlik_islemi.signOut();    //        kimlik_islemi.getInstance().signOut();    kodu da yazılabilirdi.
//        FirebaseUser user= kimlik_islemi.getCurrentUser();    // SingOut'dan sonra User kalmıyor
//        Toast.makeText(this,user.getUid(),Toast.LENGTH_SHORT).show();     // Bu kod hata verir

        Intent intent = new Intent(Yonetici_Paneli.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
