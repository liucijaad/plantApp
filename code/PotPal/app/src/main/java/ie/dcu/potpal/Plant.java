package ie.dcu.potpal;

import java.util.List;

public class Plant {
    private String scientificName;
    private List<String> commonNames;
    private String environment;
    private String type;
    private String imageUri;

    public Plant(String scientificName, List<String> commonNames) {
        this.scientificName = scientificName;
        this.commonNames = commonNames;
        this.environment = "N/A";
        this.type = "N/A";
        this.imageUri = "N/A";
    }
    public Plant(String scientificName, String environment, String plantType, String imageUri) {
        this.scientificName = scientificName;
        this.environment = environment;
        this.type = plantType;
        this.imageUri = imageUri;
    }

    public Plant(String scientificName, List<String> commonNames, String environment, String plantType, String imageUri) {
        this.scientificName = scientificName;
        this.commonNames = commonNames;
        this.environment = environment;
        this.type = plantType;
        this.imageUri = imageUri;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public List<String> getCommonNames() {
        return commonNames;
    }

    public void setCommonNames(List<String> commonNames) {
        this.commonNames = commonNames;
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
