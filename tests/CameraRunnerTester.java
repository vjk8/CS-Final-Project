package tests;
import components.*;

public class CameraRunnerTester {

    public static void unthreadedTest() {
        CameraRunner cr = new CameraRunner();
        cr.run();
        System.out.println(cr.getCompositeFrame().getTimestampList());
    }

    public static void threadedTest() {
        ThreadedCameraRunner tcr = new ThreadedCameraRunner();
        long executeStartTime = System.currentTimeMillis();
        tcr.execute();
        while (System.currentTimeMillis() - executeStartTime <= 20000) {
            // stall
        }
        tcr.receiveMessage("STOP");
    }
    public static void main(String[] args) {
        unthreadedTest();
    }
}
