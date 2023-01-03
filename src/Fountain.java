//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title:    P02 Water Fountain
// Course:   CS 300 Spring 2022
//
// Author:   Prasoon Tandon
// Email:    ptandon3@wisc.edu
// Lecturer: Mouna Kacem
//
///////////////////////// ALWAYS CREDIT OUTSIDE HELP //////////////////////////
//
// Persons:         NONE
// Online Sources:  NONE
//
///////////////////////////////////////////////////////////////////////////////

import java.util.Random;
import java.io.File;
import processing.core.PImage;

/**
 * The main class in program about a simple animation of a water fountain.
 * 
 * @author prasoontandon
 */
public class Fountain {
  
  private static Random randGen;
  
  private static int positionX;
  private static int positionY;
  private static PImage fountainImage;
  
  private static int startColor;
  private static int endColor;
  
  private static Droplet[] droplets;

  /**
   * Main method
   * 
   * @param args input arguments if any
   */
  public static void main(String[] args) {
    Utility.runApplication(); // starts the application
  }

  /**
   * This method is automatically called by Utility.runApplication() when the program begins. It
   * creates and initializes the different data fields defined in the program, and configures the
   * different graphical settings of the application, such as loading the background image, setting
   * the dimensions of the display window, etc.
   */
  public static void setup() {
    // Test cases call
    System.out.println("testUpdateDroplet(): " + testUpdateDroplet());
    System.out.println("testRemoveOldDroplets(): " + testRemoveOldDroplets());

    // Initialization of private fields & graphical settings
    randGen = new Random();

    positionX = Utility.width() / 2;
    positionY = Utility.height() / 2;
    fountainImage = Utility.loadImage("images" + File.separator + "fountain.png");

    startColor = Utility.color(23, 141, 235);
    endColor = Utility.color(23, 200, 255);

    droplets = new Droplet[800];
  }
  
  /**
   * This method continuously executes the lines of code contained inside its block until the
   * program is stopped. It continuously draws the application display window and updates its
   * content with respect to any change or any event which affects its appearance.
   */
  public static void draw() {
    Utility.background(Utility.color(253, 245, 230));
    Utility.image(fountainImage, positionX, positionY);

    createNewDroplets(10);
    for (int i = 0; i < droplets.length; ++i) {
      if (droplets[i] != null) {
        updateDroplet(i);
      }
    }
    removeOldDroplets(80);
  }

  /**
   * The block of code defined in this method is automatically executed each time the mouse bottom
   * is pressed.
   */
  public static void mousePressed() {
    positionX = Utility.mouseX();
    positionY = Utility.mouseY();
  }
  
  /**
   * The block of code defined in this method is automatically executed each time a key is pressed.
   * 
   * @param key the character on a keyboard pressed during execution of program
   */
  public static void keyPressed(char key) {
    if (key == 's' || key == 'S') {
      Utility.save("screenshot.png");
    }
  }
  
  /**
   * This method does all of the moving (by setting positions x and y), accelerating (updating the
   * y-velocity by adding 0.3f to it), and drawing of a droplet (by calling Utility.fill() and
   * Utility.circle()), with whatever droplet is specified through the provided index.
   * 
   * @param index represents the index of a specific droplet within the droplets array.
   */
  private static void updateDroplet(int index) {
    Utility.fill(droplets[index].getColor(), droplets[index].getTransparency());

    // Set Initial Position of droplet
    Utility.circle(droplets[index].getPositionX(), droplets[index].getPositionY(),
        droplets[index].getSize());

    // Accelerating
    droplets[index].setVelocityY(droplets[index].getVelocityY() + 0.3f);

    // Moving
    droplets[index].setPositionX(droplets[index].getPositionX() + droplets[index].getVelocityX());
    droplets[index].setPositionY(droplets[index].getPositionY() + droplets[index].getVelocityY());
    
    //Update age
    droplets[index].setAge(droplets[index].getAge() + 1);
  }
  
  /**
   * Adds new Droplet instances to null references in droplets array. The method does so until
   * either: 
   * 1. it has stepped through all valid indexes and there are no more to check, or 
   * 2. it has created the specified number of new droplets and has stored references to them 
   * within the droplets array
   * 
   * @param numOfDroplets number of new droplets to create
   */
  private static void createNewDroplets(int numOfDroplets) {
    int currIndex = 0;
    while (numOfDroplets > 0 && currIndex < droplets.length) {
      if (droplets[currIndex] == null) {

        float xPosition = positionX + randGen.nextFloat() * 6 - 3;
        float yPosition = positionY + randGen.nextFloat() * 6 - 3;
        int size = randGen.nextInt(8) + 4;
        int color = Utility.lerpColor(startColor, endColor, randGen.nextFloat());
        float xVelocity = randGen.nextFloat() * 2 - 1;
        float yVelocity = randGen.nextFloat() * (-5) - 5;
        int age = randGen.nextInt(41);
        int transparency = randGen.nextInt(96) + 32;

        droplets[currIndex] = new Droplet(xPosition, yPosition, size, color);
        droplets[currIndex].setVelocityX(xVelocity);
        droplets[currIndex].setVelocityY(yVelocity);
        droplets[currIndex].setAge(age);
        droplets[currIndex].setTransparency(transparency);

        --numOfDroplets;
      }
      ++currIndex;
    }
  }

  /**
   * Searches through the droplets array for references to droplets with an age that is greater than
   * the specified max age and remove them. Removing an old droplet simply means changing the
   * reference in the array to null.
   * 
   * @param maxAge the maximum age of droplets allowed to stay in the droplets array
   */
  private static void removeOldDroplets(int maxAge) {
    for (int i = 0; i < droplets.length; ++i) {
      if (droplets[i] != null && droplets[i].getAge() > maxAge) {
        droplets[i] = null;
      }
    }
  }

  /**
   * test method for updateDroplet() in Fountain class
   *
   * @return true when no defect is found, and false otherwise
   */
  private static boolean testUpdateDroplet() {
    float xPosition = 3.0f;
    float yPosition = 3.0f;
    Droplet testDrop = new Droplet(xPosition, yPosition, 10, Utility.color(23, 141, 235));
    testDrop.setVelocityX(-1.0f);
    testDrop.setVelocityY(-2.0f);

    droplets = new Droplet[] {testDrop, null, null};
    updateDroplet(0);

    if (Math.abs(testDrop.getPositionX() - 2.0) > 0.0001
        || Math.abs(testDrop.getPositionY() - 1.3) > 0.0001) {

      System.out.println("Your updateDroplet method failed to return expected output "
          + "when passed (-1,-2) as the velocity " + "on testDrop");
      return false;
    }
    return true;
  }

  /**
   * test method for removeOldDroplets() in Fountain class
   *
   * @return true when no defect is found, and false otherwise
   */
  private static boolean testRemoveOldDroplets() {
    droplets = new Droplet[6];

    Droplet testDrop0 = new Droplet();
    testDrop0.setAge(7); // Will be removed

    Droplet testDrop1 = new Droplet();
    testDrop1.setAge(80); // Will be removed

    Droplet testDrop2 = new Droplet();
    testDrop2.setAge(6); // Will NOT be removed

    droplets[0] = testDrop0;
    droplets[1] = testDrop1;
    droplets[2] = testDrop2;

    removeOldDroplets(6);
    if (droplets[0] != null || droplets[1] != null || droplets[2] != testDrop2) {
      System.out.println("Your removeOldDroplets method failed to return expected output "
          + "when passed age 6 as the max age " + "on testDrop0, testDrop1, and testDrop2");

      return false;
    }
    return true;
  }
}
