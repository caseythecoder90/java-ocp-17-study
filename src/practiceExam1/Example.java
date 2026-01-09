package practiceExam1;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Example {
}
class Ostrich {
    private String color;

    public static void main(String[] args) {
        Ostrich ostrich = new Ostrich();
        System.out.println(ostrich.color);
        double key = (int)5f;
        long note = (char)(byte)key++;
        double song = note % 2;
        System.out.print(key+", "+note+", "+song);



    }
}


class Clock {
    private void printCurrentTime(LocalTime time) {
        var f = DateTimeFormatter.ofPattern("'It''s 'h' hours past midnight, and 'mm' minutes'");
        System.out.print(f.format(time));
    }
    public static void main(String[] unused) {
        new Clock().printCurrentTime(LocalTime.of(4, 5));
    } }