/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.zavadil.prototype1;

/**
 *
 * @author karel
 */
public class Main {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.processPicture(new PictureItem("TEST"));
        controller.processPicture(new PictureItem("22222"));
        controller.processPicture(new PictureItem("333 333 333"));
    }
    
}
