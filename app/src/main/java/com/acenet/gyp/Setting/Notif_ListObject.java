package com.acenet.gyp.Setting;

/**
 * Created by AceNet on 1/11/2016.
 */
public class Notif_ListObject {
    private int interval;
    private int berapa_kali;
    private String nama_tanaman;
    private String judul;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getBerapa_kali() {
        return berapa_kali;
    }

    public void setBerapa_kali(int berapa_kali) {
        this.berapa_kali = berapa_kali;
    }

    public String getNama_tanaman() {
        return nama_tanaman;
    }

    public void setNama_tanaman(String nama_tanaman) {
        this.nama_tanaman = nama_tanaman;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }
}
