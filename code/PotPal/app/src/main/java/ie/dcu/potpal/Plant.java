package ie.dcu.potpal;

public class Plant {
    private String scientificName;
    private String type;
    private String sunlight;
    private String water;
    private String commonNames;
    private String photoFilePath;
    public Plant(String scientificName, String type, String sunlight, String water, String commonNames, String photoFilePath) {
        this.scientificName = scientificName;
        this.type = type;
        this.sunlight = sunlight;
        this.water = water;
        this.commonNames = commonNames;
        this.photoFilePath = photoFilePath;
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
}
