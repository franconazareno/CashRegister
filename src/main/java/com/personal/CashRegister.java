package com.personal;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CashRegister {

    private static String cmd = "usage";
    private static final int[] initCount = { 1, 2, 3, 4, 5 };
    private static final int[] denominations = { 20, 10, 5, 2, 1 };
    private int[] counter;
    private int[] values;

    /**
     * Check command
     * @return boolean
     */
    private boolean evaluateCmd() {
        return !cmd.equals("quit");
    }

    /**
     * Initialize variables
     */
    private void init() {
        counter = new int[5];
        values = new int[5];
        counter = initCount.clone();
    }

    /**
     * Show current state, including total and count of each denomination
     */
    private void show() {
        calculateValues();
        int sum = calculateSum(values);
        showFormatted(sum);
    }

    /**
     * Exit the program
     */
    private void quit() {
        System.out.println("Bye");
        System.exit(0);
    }

    private void usage() {
        System.out.println("Usage: cmd args" +
                "\nCommands and arguments:" +
                "\ninit - initialize denomination count and values" +
                "\nshow - show current state, including total and each denomination" +
                "\nput args - 5 arguments separated by space, put bills in each denomination and show current state" +
                "\ntake args - 5 arguments separated by space, take bills in each denomination and show current state" +
                "\nchange args - 1 argument, show requested change in each denomination, remove from cash register and show current state" +
                "\nquit - exit program" +
                "\nhelp - show usage"
        );
    }

    /**
     * Format the result
     * @param sum
     */
    private void showFormatted(int sum) {
        String toPrint = "";
        for (int i = 0; i < counter.length; i++) {
            toPrint += " " + counter[i];
        }
        System.out.println("$" + sum + toPrint);
    }

    /**
     * Calculate the sum of values of the denominations
     * @param value
     * @return
     */
    private int calculateSum(int[] value) {
        int sum = 0;
        for (int i = 0; i < value.length; i++) {
            sum += value[i];
        }
        return sum;
    }

    /**
     * Calculate the values of each denominations and store in array
     */
    private void calculateValues() {
        for (int i = 0; i < counter.length; i++) {
            values[i] = denominations[i] * counter[i];
        }
    }

    /**
     * Main entry
     */
    public static void main(String[] args) {
        CashRegister register = new CashRegister();
        register.init();
        while (register.evaluateCmd()) {
            try {
                BufferedReader bufferedReader = new BufferedReader (new InputStreamReader(System.in));
                String[] words = bufferedReader.readLine().split(" ");
                cmd = words[0];
                if (cmd.equals("init")) {
                    register.init();
                } else if (cmd.equals("show")) {
                    register.show();
                } else if (cmd.equals("quit")) {
                    register.quit();
                } else {
                    register.usage();
                }
            } catch (Exception exception) {
                // Handle exceptions here.
            }
        }
    }
}
