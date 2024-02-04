package ie.dcu.potpal;

public class Plant {
    private String plantName;
    private String environment;
    private String type;
    private String imageUri;


    public Plant(String plantName, String environment, String plantType, String imageUri) {
        this.plantName = plantName;
        this.environment = environment;
        this.type = plantType;
        this.imageUri = imageUri;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String PlantName) {
        this.plantName = PlantName;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
