//package com.example;
//
//public class TestPassword {
//
//}

package com.example; // use your package name

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestPassword {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String encoded = "$2a$10$8K1p/a0dhrxSHxN1X6A7.0lRqS8deMVGxFWpBLCdxhpbSoVqjvVsS";

        System.out.println("admin → " + encoder.matches("admin", encoded));
        System.out.println("password123 → " + encoder.matches("password123", encoded));
        System.out.println(new BCryptPasswordEncoder().encode("admin"));
    }
}