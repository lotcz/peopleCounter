package eu.zavadil.prototype1;

public class Main {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ControllerSettings settings = new ControllerSettings();
       
        /* Face++ providers */
        settings.getFaceDetectorSettings().face_detection_provider = FaceDetectorProvidersEnum.FACEPP;
        settings.getFaceMatcherSettings().face_matching_provider = FaceMatcherProvidersEnum.FACEPP;
               
        Controller controller = new Controller(settings);
        controller.processPicturesFolder("C:\\develop\\peopleCounter\\test\\pictures\\set1");
        
    }
    
}
