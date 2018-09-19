package eu.zavadil.prototype1;

/**
 * Face detector runner.
 */
public class FaceDetector implements Runnable {
    private Thread t;
    private final PictureItem picture;
    private Runnable on_finished_runnable;
    private Runnable on_error_runnable;
    
    FaceDetector(PictureItem item, Runnable on_finished, Runnable on_error) {
      picture = item;
      on_finished_runnable = on_finished;
      on_error_runnable = on_error;
   }
   
   public void start () {
      //System.out.println("FaceDetector: Starting detection " + this.picture );
      if (t == null) {
         t = new Thread(this);
         t.start ();
      }
   }
   
   @Override
   public void run() {      
      try {         
        picture.faces_detected = 4;
        // Let the thread sleep for a while.
        Thread.sleep(5000);        
      } catch (InterruptedException e) {
         System.out.println("FaceDetector: Error.");
         if (on_error_runnable != null) {
          on_error_runnable.run();                 
      }
      }
      //System.out.println("FaceDetector: Finished.");
      if (on_finished_runnable != null) {
          on_finished_runnable.run();                 
      }
   } 
   
}
