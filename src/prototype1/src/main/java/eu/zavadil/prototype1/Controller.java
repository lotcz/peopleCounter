package eu.zavadil.prototype1;

import java.io.File;

/**
 * Core of peopleCounter.
 */
public class Controller {

    public FacesCollection unique_faces = new FacesCollection();
    
    Controller() {

    }

    public void processPicturesFolder(String path) {
        File folder = new File(path);
        File[] files = folder.listFiles();

        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                processPictureFile(files[i].getPath());
            } else if (files[i].isDirectory()) {
                System.out.println("Directory " + files[i].getName());
            }
        }

    }

    public void processPictureFile(String path) {
        processPicture(new PictureItem(path));
    }

    private void processPicture(PictureItem picture) {
        System.out.println("Controller: Processing image " + picture);
        processDetectionQueue(picture);
    }

    /* FACE DETECTION */
    private FaceDetector detector;
    private final PicturesQueue detection_queue = new PicturesQueue();

    private void processDetectionQueue(PictureItem picture) {
        if (detector == null) {
            if (!detection_queue.isEmpty()) {
                if (picture != null) {
                    detection_queue.enqueue(picture);
                }
                picture = detection_queue.dequeue();
            }
            if (picture != null) {
                detectFaces(picture);
            }
        } else {
            if (picture != null) {
                detection_queue.enqueue(picture);
            }
        }
    }

    void detectFaces(PictureItem picture) {

        Runnable on_success = new Runnable() {

            @Override
            public void run() {
                detector = null;
                onFacesDetected(picture);
            }

        };

        Runnable on_error = new Runnable() {

            @Override
            public void run() {
                detector = null;
                onFacesDetectionError(picture);
            }

        };

        detector = new FaceDetector(picture, on_success, on_error);
        detector.start();

    }

    void onFacesDetected(PictureItem picture) {
        System.out.println("Controller: Image face detection completed " + picture);
        if (picture.faces_detected.size() > 0) {
            processMatchingQueue(picture);
        }
        processDetectionQueue(null);
    }

    void onFacesDetectionError(PictureItem picture) {
        System.out.println("Controller: Image face detection failed " + picture);
        processDetectionQueue(null);
    }

    /* FACE MATCHING */
    private final PicturesQueue matching_queue = new PicturesQueue();
    private FaceMatcher matcher;

    private void processMatchingQueue(PictureItem picture) {
        if (matcher == null) {
            if (!matching_queue.isEmpty()) {
                if (picture != null) {
                    matching_queue.enqueue(picture);
                }
                picture = matching_queue.dequeue();
            }
            if (picture != null) {
                matchFaces(picture);
            }
        } else {
            if (picture != null) {
                matching_queue.enqueue(picture);
            }
        }
    }

    void matchFaces(PictureItem picture) {
        Runnable on_success = new Runnable() {

            @Override
            public void run() {
                matcher = null;
                onFacesMatched(picture);
            }

        };

        Runnable on_error = new Runnable() {

            @Override
            public void run() {
                matcher = null;
                onFaceMatchingError(picture);
            }

        };

        matcher = new FaceMatcher(picture, unique_faces, on_success, on_error);
        matcher.start();
    }

    void onFacesMatched(PictureItem picture) {
        System.out.println("Controller: Image face matching completed " + picture);
        unique_faces.addAll(picture.faces_unmatched);
        System.out.println("Controller: Unique faces - " + unique_faces.size());
        processMatchingQueue(null);
    }

    void onFaceMatchingError(PictureItem picture) {
        System.out.println("Controller: Image face matching failed " + picture);
        processMatchingQueue(null);
    }
}
