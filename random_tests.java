public class random_tests {
    // testing out millisecond timing
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        System.out.println("START: " + start);
        int junk = 0;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if (i % 2 == 0) junk++;
            else junk--;
        }
        long finish = System.currentTimeMillis();
        System.out.println("FINISH: " + finish);
        System.out.println(finish-start);
    }
}
