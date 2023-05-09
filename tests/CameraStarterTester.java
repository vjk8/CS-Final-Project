package tests;

import components.CameraStarter;

public class CameraStarterTester
{

    public static void test1()
    {
        CameraStarter starter = new CameraStarter(196); // sound-based
        System.out.println("Block for 10 seconds while waiting for sound");
        long result = starter.getStartTime();
        System.out.println("Started at time " + result);
        // Works
    }


    public static void test2()
    {
        CameraStarter starter = new CameraStarter(false); // sound-based
        System.out.println("Block for 10 seconds while waiting for keypress");
        long result = starter.getStartTime();
        System.out.println("Started at time " + result);
        // Does not work
    }


    public static void main(String[] args)
    {
        test1();
    }
}
