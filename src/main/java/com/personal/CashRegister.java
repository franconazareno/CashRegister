package com.personal;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CashRegister {

    private static String cmd = "usage";
    private static final int[] initCount = { 1, 2, 3, 4, 5 };
    private static final int[] denominations = { 20, 10, 5, 2, 1 };
    private int[] counts;
    private int[] values;
    private int[] cloneCount;

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
        counts = new int[5];
        values = new int[5];
        cloneCount = new int[5];
        counts = initCount.clone();
        System.out.println("ready");
    }

    /**
     * Show current state, including total and count of each denomination
     */
    private void show() {
        calculateValues();
        showFormatted(calculateSum(values));
    }

    /**
     * Put bills in each denomination and show current state
     * @param args
     */
    private void put(String[] args) {
        try {
            cloneCount = counts.clone();
            for (int i = 0; i < counts.length; i++) {
                int cashToPut = Integer.parseInt(args[i+1]);
                if (cashToPut >= 0) {
                    counts[i] += cashToPut;
                } else {
                    counts = cloneCount.clone();
                    System.out.println("Cannot accept negative integers - for input string: \"" + cashToPut + "\"");
                    break;
                }
            }
            calculateValues();
            showFormatted(calculateSum(values));
        } catch (NumberFormatException exception) {
            counts = cloneCount.clone();
            System.out.println("Accepts integers only - " + exception.getMessage().toLowerCase());
        }
    }

    /**
     * Take bills in each denomination and show current state
     * @param args
     */
    private void take(String[] args) {
        try {
            cloneCount = counts.clone();
            for (int i = 0; i < counts.length; i++) {
                int cashToTake = Integer.parseInt(args[i+1]);
                if (cashToTake >= 0 && cashToTake <= counts[i]) {
                    counts[i] -= Integer.parseInt(args[i+1]);
                } else {
                    counts = cloneCount.clone();
                    System.out.println("Cannot accept negative/greater than count integers - for input string: \"" + cashToTake + "\"");
                    break;
                }
            }
            calculateValues();
            showFormatted(calculateSum(values));
        } catch (NumberFormatException exception) {
            counts = cloneCount.clone();
            System.out.println("Accepts integers only - " + exception.getMessage().toLowerCase());
        }
    }

    private void change(String[] args) {
        //TODO
        System.out.println("change");
    }

    /**
     * Exit the program
     */
    private void quit(int status) {
        System.out.println("Bye");
        System.exit(status);
    }

    /**
     * Print usage help
     */
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
        for (int i = 0; i < counts.length; i++) {
            toPrint += " " + counts[i];
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
        for (int i = 0; i < counts.length; i++) {
            values[i] = denominations[i] * counts[i];
        }
    }

    /**
     * Main entry for cash register app
     * @param args
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
                } else if (cmd.equals("put")) {
                    register.put(words);
                } else if (cmd.equals("take")) {
                    register.take(words);
                } else if (cmd.equals("change")) {
                    register.change(words);
                } else if (cmd.equals("quit")) {
                    register.quit(0);
                } else {
                    register.usage();
                }
            } catch (Exception exception) {
                System.out.println("System error: " + exception.getMessage());
                System.out.println("Exiting..");
                register.quit(1);
            }
        }
    }
}
