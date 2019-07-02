package com.example.ozgun.bitirmeprojesi;

import java.io.Serializable;

public class dbProjeler implements Serializable
{
    public String Proje_ID;
    public String Proje_Adi;
    public String Proje_Baslangic_Tarihi;

    public dbProjeler()
    {

    }

    public dbProjeler(String Proje_ID, String Proje_Adi, String Proje_Baslangic_Tarihi)
    {
        this.Proje_ID = Proje_ID;
        this.Proje_Adi = Proje_Adi;
        this.Proje_Baslangic_Tarihi = Proje_Baslangic_Tarihi;
    }

    // Set Metodları
    public void setProje_ID(String proje_ID)
    {
        Proje_ID = proje_ID;
    }

    public void setProje_Adi(String proje_Adi)
    {
        Proje_Adi = proje_Adi;
    }

    public void setProje_Baslangic_Tarihi(String proje_Baslangic_Tarihi)
    {
        Proje_Baslangic_Tarihi = proje_Baslangic_Tarihi;
    }

    // Get Metodları
    public String getProje_ID()
    {
        return Proje_ID;
    }

    public String getProje_Adi()
    {
        return Proje_Adi;
    }

    public String getProje_Baslangic_Tarihi()
    {
        return Proje_Baslangic_Tarihi;
    }
}
