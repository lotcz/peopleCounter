package eu.zavadil.prototype1;

/**
 * Face matcher runner.
 */
public class FaceMatcher implements Runnable {
    private Thread t;
    private final PictureItem picture;
    private final Runnable on_finished_runnable;
    private final Runnable on_error_runnable;
    
    FaceMatcher(PictureItem item, Runnable on_finished, Runnable on_error) {
      picture = item;
      on_finished_runnable = on_finished;
      on_error_runnable = on_error;
   }
   
   public void start () {
      //System.out.println("FaceMatcher: Starting face matching " + this.picture );
      if (t == null) {
         t = new Thread(this);
         t.start ();
      }
   }
   
   @Override
   public void run() {      
      try {         
        picture.faces_matched = 2;
        // Let the thread sleep for a while.
        Thread.sleep(1500);        
      } catch (InterruptedException e) {
         System.out.println("FaceMatcher: Error.");
         if (on_error_runnable != null) {
            on_error_runnable.run();                 
        }
      }
      //System.out.println("FaceMatcher: Finished.");
      if (on_finished_runnable != null) {
          on_finished_runnable.run();                 
      }
   } 
   
}
