package practicetests.ch01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Chapter01 {

    // text blocks
    // - essential and nonessential whitespace

    // rules for variable names

    // Duration and Period

    // operators like bitwise complement, and other dumb operators
    //  operator precedence

    // Long.parseLong returns a primitive type and not Long

    public static void main(String[] args) {
        int time = 9;
        int day = 3;
        var dinner = ++time >= 10 ? day-- <= 2
                ? "Takeout" : "Salad" : "Leftovers";
        System.out.println((1+1)*(1+2));
        List<String> cats = new ArrayList<>();
        cats.add("leo");
        cats.add("Olivia");
        cats.sort((c1, c2) -> -c2.compareTo(c1));
        Collections.reverse(cats);
        System.out.println(cats);

        Consumer<String> supplier = (var s) -> System.out.println(s);

        Integer data = 5; // cannot do this below
//        if (data instanceof Integer num) {
//
//        }
    }

    // which data types does switch statement allow?
    // - byte, short, char, int, String, enum plus
    // the corresponding wrapper classes Character, Byte, Integer, Short
    Short num = 5;


}
