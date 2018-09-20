package eu.zavadil.prototype1;

/**
 * Settings for Controller.
 */
public class ControllerSettings {
    
    /**
     * Base folder for all program data.
     */
    public String working_directory;
    
    public ControllerSettings() {
        working_directory = System.getProperty("user.dir");
    }
    
    private FaceDetectorSettings face_detector_settings;

    public FaceDetectorSettings getFaceDetectorSettings() {
        if (face_detector_settings == null) {
            face_detector_settings = new FaceDetectorSettings();
        }
        return face_detector_settings;
    }

    private FaceMatcherSettings face_matcher_settings;
    
    public FaceMatcherSettings getFaceMatcherSettings() {
        if (face_matcher_settings == null) {
            face_matcher_settings = new FaceMatcherSettings();
        }
        return face_matcher_settings;
    }
    
}
