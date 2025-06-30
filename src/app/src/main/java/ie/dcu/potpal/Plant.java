package ie.dcu.potpal;

import java.io.Serializable;

public class Plant implements Serializable {
    private Integer id;
    private String scientificName;
    private String type;
    private String sunlight;
    private String water;
    private String commonNames;
    private String photoFilePath;
    private String lastWatered;

    public Plant(Integer id, String scientificName, String type, String sunlight, String water, String commonNames, String photoFilePath, String lastWatered) {
        this.id = id;
        this.scientificName = scientificName;
        this.type = type;
        this.sunlight = sunlight;
        this.water = water;
        this.commonNames = commonNames;
        this.photoFilePath = photoFilePath;
        this.lastWatered = lastWatered;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSunlight() {
        return sunlight;
    }

    public void setSunlight(String sunlight) {
        this.sunlight = sunlight;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getCommonNames() {
        return commonNames;
    }

    public void setCommonNames(String commonNames) {
        this.commonNames = commonNames;
    }

    public String getPhotoFilePath() {
        return photoFilePath;
    }

    public void setPhotoFilePath(String photoFilePath) {
        this.photoFilePath = photoFilePath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastWatered() {
        return lastWatered;
    }

    public void setLastWatered(String lastWatered) {
        this.lastWatered = lastWatered;
    }
}
