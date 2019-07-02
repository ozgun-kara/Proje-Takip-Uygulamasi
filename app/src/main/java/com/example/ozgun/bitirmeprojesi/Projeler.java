package com.example.ozgun.bitirmeprojesi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import java.util.ArrayList;

public class Projeler extends AppCompatActivity
{
    private FirebaseDatabase db;
    private FirebaseAuth kimlik_islemi;
    private FirebaseUser mevcut_kullanici;
    private String mail;
    private String UID;

    private ArrayList<dbProjeler> Projeler_Listesi = new ArrayList<dbProjeler> ();
    private ArrayList<String> Proje_Isimleri_Listesi = new ArrayList<String> ();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projeler);

        getSupportActionBar().setTitle("Projeler");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        kimlik_islemi = FirebaseAuth.getInstance();
        mevcut_kullanici = kimlik_islemi.getCurrentUser();
        UID = mevcut_kullanici.getUid();

        // Yonetici_Paneli'nden gelen parametrelerin karşılanması
        Projeler_Listesi = (ArrayList<dbProjeler>)getIntent().getSerializableExtra("Parametre-1");
        Proje_Isimleri_Listesi = (ArrayList<String>)getIntent().getSerializableExtra("Parametre-2");

        db = FirebaseDatabase.getInstance();    // Uygulama database'e bağlandı.

//        Toast.makeText(this, "Mevcut Kullanıcı UID = " + mevcut_kullanici.getUid(), Toast.LENGTH_SHORT).show();

        //  1. adım
        final ListView Projeler_Listview = (ListView) findViewById(R.id.listView1);
        //  2. adım
        ArrayAdapter <String> veriAdaptoru = new ArrayAdapter <String> (this, android.R.layout.simple_list_item_1, android.R.id.text1, Proje_Isimleri_Listesi);
        //  3. adım
        Projeler_Listview.setAdapter(veriAdaptoru);

        //  Listview elemanlarına tıklama olayları
        Projeler_Listview.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
//                AlertDialog.Builder diyalogOlusturucu = new AlertDialog.Builder(Projeler.this);
//
//                diyalogOlusturucu.setMessage(Projeler_Listesi[position])
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

                Object o = Projeler_Listview.getItemAtPosition(position);
                Toast.makeText(Projeler.this, o.toString(), Toast.LENGTH_SHORT).show();
            }



        });
    }

    // + Butonunun çalışması...
    public void Proje_Olustur(View v)
    {
        Intent intent = new Intent(this, Yeni_Proje_Olustur.class);
        startActivity(intent);
        finish();
    }

}
