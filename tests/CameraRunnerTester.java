package tests;
import components.*;

public class CameraRunnerTester {
    public static void main(String[] args)
    {
        CameraRunner cr = new CameraRunner();
        cr.run();
        System.out.println(cr.getCompositeFrame().getTimestampList());
    }
}
