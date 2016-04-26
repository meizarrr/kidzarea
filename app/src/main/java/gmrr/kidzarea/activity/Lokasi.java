package gmrr.kidzarea.activity;

import android.location.Location;

/**
 * Created by Rifqi Zuliansyah on 15/04/2016.
 */
public class Lokasi {

    private String unique_id;
    private String name;
    private double longitude;
    private double latitude;

    private String waktu;


    public Lokasi(Location location) {
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
    }

    public Lokasi(String name, double longitude, double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Lokasi(String name, String unique_id, double longitude, double latitude) {
        this(name, longitude, latitude);
        this.unique_id = unique_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }
}