package model;

/**
 * Created by ushtinfeld on 15/03/2018.
 */

public class Meeting
{
    private String title;
    private String description;
    private double longitude;
    private String locationName;
    private double latitude;
    private String id;
    public Meeting(){

    }
    public Meeting(String id,String title, String description, String locationName, double latitude, double longitude) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.longitude = longitude;
        this.locationName = locationName;
        this.latitude = latitude;
    }

    public String getId() {
        return id;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }


    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public String getLocationName ()
    {
        return locationName;
    }

    public void setLocationName (String locationName)
    {
        this.locationName = locationName;
    }

    public double getLatitude ()
    {
        return latitude;
    }

    public void setLatitude (double latitude)
    {
        this.latitude = latitude;
    }

    @Override
    public String toString()
    {
        return "title = "+title+", description = "+description+", locationName = "+locationName;
    }
}
