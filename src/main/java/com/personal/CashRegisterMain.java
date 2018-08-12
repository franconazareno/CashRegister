package com.personal;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CashRegisterMain {

    /**
     * Main entry for cash register app
     * @param args
     */
    public static void main(String[] args) {
        CashRegister register = new CashRegister();
        register.init();

        try {
            while (register.evaluateCmd()) {
                BufferedReader bufferedReader = new BufferedReader (new InputStreamReader(System.in));
                String[] words = bufferedReader.readLine().split(" ");
                if (words.length > 0) {
                    register.cmd = words[0];
                    register.executeCmd(words);
                } else {
                    register.usage();
                }
            }
        } catch (Exception exception) {
            System.out.println("System error: " + exception.getMessage());
            System.out.println("Exiting...");
            register.quit(1);
        }
    }
}
