package tests;
// some git error
import org.junit.Assert.*;
import org.junit.Test;
import components.CameraStarter;

public class CameraStarterTester {

    public static void main(String[] args) {
        CameraStarter starter = new CameraStarter(128); // sound-based
        System.out.println("Block for 10 seconds while waiting for sound");
        long result = starter.getStartTime();
        System.out.println("Started at time " + result);
        // Works
    }
}
