package com.example.ozgun.bitirmeprojesi;

import java.io.Serializable;

public class dbCalisanlar implements Serializable
{
    public String Calisan_ID;
    public String Calisan_Adi;
    public String Calisan_Baslangic_Tarihi;

    public dbCalisanlar()
    {

    }

    public dbCalisanlar(String Calisan_ID, String Calisan_Adi, String Calisan_Baslangic_Tarihi)
    {
        this.Calisan_ID = Calisan_ID;
        this.Calisan_Adi = Calisan_Adi;
        this.Calisan_Baslangic_Tarihi = Calisan_Baslangic_Tarihi;
    }

    // Set Metodları
    public void setCalisan_ID(String calisan_ID)
    {
        Calisan_ID = calisan_ID;
    }

    public void setCalisan_Adi(String calisan_Adi)
    {
        Calisan_Adi = calisan_Adi;
    }

    public void setCalisan_Baslangic_Tarihi(String calisan_Baslangic_Tarihi)
    {
        Calisan_Baslangic_Tarihi = calisan_Baslangic_Tarihi;
    }

    // Get Metodları
    public String getCalisan_ID()
    {
        return Calisan_ID;
    }

    public String getCalisan_Adi()
    {
        return Calisan_Adi;
    }

    public String getCalisan_Baslangic_Tarihi()
    {
        return Calisan_Baslangic_Tarihi;
    }
}
