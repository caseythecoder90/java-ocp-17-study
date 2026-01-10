package practiceExam1;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Example {
}
class Ostrich {
    private String color;

}


class Clock {
    private void printCurrentTime(LocalTime time) {
        var f = DateTimeFormatter.ofPattern("'It''s 'h' hours past midnight, and 'mm' minutes'");
        System.out.print(f.format(time));
    }
    public static void main(String[] unused) {
        new Clock().printCurrentTime(LocalTime.of(4, 5));
    } }