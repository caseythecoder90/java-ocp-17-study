package jdbc;


import java.util.Arrays;

public class Example {

    private String name = "Example";
    static String year = "1997";

    {
        name = "example";
        year = "1994";
    }

    static {
        year = "1994";
    }

    public static void main(String[] args) {

        Object object = (true ? false : true ? 5 : 6);

        int[] x = { 7, 8, 9};
        int[] y = { 7, 7, 7};
        System.out.println(Arrays.compare(x, y) + " " + Arrays.mismatch(x, y));

    }
}
