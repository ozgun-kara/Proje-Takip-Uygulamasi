package com.example.ozgun.bitirmeprojesi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import java.util.ArrayList;

public class Calisanlar extends AppCompatActivity
{
    private FirebaseDatabase db;
    private FirebaseAuth kimlik_islemi;
    private FirebaseUser mevcut_kullanici;
    private String mail;
    private String UID,yonetici_maili,yonetici_sifresi;

    private ArrayList<dbCalisanlar> Calisanlar_Listesi = new ArrayList<dbCalisanlar> ();
    private ArrayList<String> Calisan_Isimleri_Listesi = new ArrayList<String> ();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calisanlar);

        getSupportActionBar().setTitle("Çalışanlar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        kimlik_islemi = FirebaseAuth.getInstance();
        mevcut_kullanici = kimlik_islemi.getCurrentUser();
        UID = mevcut_kullanici.getUid();

        // Yonetici_Paneli'nden gelen parametrelerin karşılanması
        Calisanlar_Listesi = (ArrayList<dbCalisanlar>)getIntent().getSerializableExtra("Parametre-1");
        Calisan_Isimleri_Listesi = (ArrayList<String>)getIntent().getSerializableExtra("Parametre-2");

        db = FirebaseDatabase.getInstance();    // Uygulama database'e bağlandı.

//        Toast.makeText(this, "Mevcut Kullanıcı UID = " + mevcut_kullanici.getUid(), Toast.LENGTH_SHORT).show();

        //  1. adım
        final ListView Calisanlar_Listview = (ListView) findViewById(R.id.listView2);
        //  2. adım
        ArrayAdapter <String> veriAdaptoru = new ArrayAdapter <String> (this, android.R.layout.simple_list_item_1, android.R.id.text1, Calisan_Isimleri_Listesi);
        //  3. adım
        Calisanlar_Listview.setAdapter(veriAdaptoru);

        //  Listview elemanlarına tıklama olayları
        Calisanlar_Listview.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
//                AlertDialog.Builder diyalogOlusturucu = new AlertDialog.Builder(Calisanlar.this);
//
//                diyalogOlusturucu.setMessage(Calisanlar_Listesi[position])
//                        .setCancelable(false)
//                        .setPositiveButton("Tamam", new DialogInterface.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which)
//                            {
//                                dialog.dismiss();
//                            }
//                        });
//                diyalogOlusturucu.create().show();

                Object o = Calisanlar_Listview.getItemAtPosition(position);
                Toast.makeText(Calisanlar.this, o.toString(), Toast.LENGTH_SHORT).show();
            }



        });


        Yonetici_Bilgileri_Datatabase_Okuma_Islemleri();
    }

    public void Yonetici_Bilgileri_Datatabase_Okuma_Islemleri()
    {
        DatabaseReference dbRef = db.getReference("KULLANICILAR/" + UID + "/Bilgileri/");

        ValueEventListener dinleyici = new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                yonetici_maili = dataSnapshot.child("Mail").getValue().toString();
                yonetici_sifresi = dataSnapshot.child("Sifre").getValue().toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        };

        dbRef.addListenerForSingleValueEvent(dinleyici);    // Veritabanını 1 kereliğine dinlettirdik.

    }

    // + Butonunun çalışması...
    public void Calisan_Olustur(View v)
    {
        Intent intent = new Intent(this, Yeni_Calisan_Olustur.class);
        intent.putExtra("Parametre-1", UID);
        intent.putExtra("Parametre-2", yonetici_maili);
        intent.putExtra("Parametre-3", yonetici_sifresi);
        startActivity(intent);
        finish();
    }

}
