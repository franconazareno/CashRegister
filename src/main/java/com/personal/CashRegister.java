package com.personal;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CashRegister {

    private static final int[] initCount = { 1, 2, 3, 4, 5 };
    private static final int[] denominations = { 20, 10, 5, 2, 1 };
    private int[] counts;
    private int[] values;
    private int[] countsClone;

    public static String cmd = "usage";

    /**
     * Check command
     * @return boolean
     */
    public boolean evaluateCmd() {
        return !cmd.equals("quit");
    }

    /**
     * Check if number of arguments needed is correct
     * @param args
     * @param count
     * @return
     */
    private boolean checkArgsCount(String[] args, int count) {
        if (args.length == count) {
            return true;
        }
        System.out.println("Incorrect number of arguments");
        return false;
    }

    /**
     * Execute the command with the given parameters
     * @param args
     */
    public void executeCmd(String[] args) {
        if (cmd.equals("init")) {
            this.init();
        } else if (cmd.equals("show")) {
            this.show();
        } else if (cmd.equals("put")) {
            if (checkArgsCount(args, 6)) this.put(args);
        } else if (cmd.equals("take")) {
            if (checkArgsCount(args, 6)) this.take(args);
        } else if (cmd.equals("change")) {
            if (checkArgsCount(args, 2)) this.change(args);
        } else if (cmd.equals("quit")) {
            this.quit(0);
        } else {
            this.usage();
        }
    }

    /**
     * Initialize variables
     */
    public void init() {
        counts = new int[5];
        values = new int[5];
        countsClone = new int[5];
        counts = initCount.clone();
        System.out.println("ready");
    }

    /**
     * Show current state, including total and count of each denomination
     * $Total #$20 #$10 #$5 #$2 #$1
     * for example, $68 1 2 3 4 5 means:
     * Total=$68 $20x1 $10x2 $5x3 $2x4 $1x5
     */
    private void show() {
        calculateValues();
        showFormatted(calculateSum(values));
    }

    /**
     * Put bills in each denomination: #$20 #$10 #$5 #$2 #$1
     * and show current state
     * @param args
     */
    private void put(String[] args) {
        try {
            countsClone = counts.clone();
            for (int i = 0; i < counts.length; i++) {
                int cashToPut = Integer.parseInt(args[i+1]);
                if (cashToPut >= 0) {
                    counts[i] += cashToPut;
                } else {
                    counts = countsClone.clone();
                    System.out.println("Cannot accept negative integers - for input string: \"" + cashToPut + "\"");
                    break;
                }
            }
            calculateValues();
            showFormatted(calculateSum(values));
        } catch (NumberFormatException exception) {
            counts = countsClone.clone();
            System.out.println("Accepts integers only - " + exception.getMessage().toLowerCase());
        }
    }

    /**
     * Take bills in each denomination: #$20 #$10 #$5 #$2 #$1
     * and show current state
     * @param args
     */
    private void take(String[] args) {
        try {
            countsClone = counts.clone();
            for (int i = 0; i < counts.length; i++) {
                int cashToTake = Integer.parseInt(args[i+1]);
                if (cashToTake >= 0 && cashToTake <= counts[i]) {
                    counts[i] -= Integer.parseInt(args[i+1]);
                } else {
                    counts = countsClone.clone();
                    System.out.println("Cannot accept negative/greater than count integers - for input string: \"" + cashToTake + "\"");
                    break;
                }
            }
            calculateValues();
            showFormatted(calculateSum(values));
        } catch (NumberFormatException exception) {
            counts = countsClone.clone();
            System.out.println("Accepts integers only - " + exception.getMessage().toLowerCase());
        }
    }

    /**
     * Remove money from cash register
     * and show requested change in each denomination: #$20 #$10 #$5 #$2 #$1
     * and show error if there are insufficient funds or no change can be made
     * @param args
     */
    private void change(String[] args) {
        try {
            countsClone = counts.clone();
            int[] tmpCount = counts.clone();
            int cashToChange = Integer.parseInt(args[1]);

            if (cashToChange > 0) {
                for (int i = 0; i < 100; i++) {
                    for (int j = 0; j < counts.length; j++) {
                        cashToChange = calculateChange(j, cashToChange);
                        if (cashToChange <= 0) {
                            break;
                        }
                    }

                    if (cashToChange == 0) {
                        showChangeUsage(tmpCount);
                        break;
                    } else if (cashToChange > 0 & i == 99) {
                        counts = tmpCount.clone();
                        System.out.println("sorry");
                        break;
                    }
                }
            } else if (cashToChange == 0) {
                counts = countsClone.clone();
                System.out.println("Nothing to change");
            } else {
                counts = countsClone.clone();
                System.out.println("Cannot accept negative integers - for input string: \"" + cashToChange + "\"");
            }
        } catch (NumberFormatException exception) {
            counts = countsClone.clone();
            System.out.println("Accepts integers only - " + exception.getMessage().toLowerCase());
        }
    }

    /**
     * Exit the program
     */
    public void quit(int status) {
        System.out.println("Bye");
        System.exit(status);
    }

    /**
     * Print usage help
     */
    public void usage() {
        System.out.println("Usage: cmd args" +
                "\nCommands and arguments:" +
                "\ninit - initialize denomination count and values" +
                "\nshow - show current state, including total and each denomination" +
                "\nput args - 5 arguments separated by space, put bills in each denomination and show current state" +
                "\ntake args - 5 arguments separated by space, take bills in each denomination and show current state" +
                "\nchange args - 1 argument, remove from cash register and show requested change in each denomination" +
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
     * Calculate and remove money from cash register
     * @param index
     * @param cashToChange
     * @return
     */
    private int calculateChange(int index, int cashToChange) {
        if (counts[index] > 0) {
            if (values[index] <= cashToChange || cashToChange % denominations[index] == 0) {
                counts[index] -= 1;
                cashToChange -= denominations[index];
                calculateValues();
            }
        }
        return cashToChange;
    }

    /**
     * Print the count of denominations used
     * @param orig
     */
    private void showChangeUsage(int[] orig) {
        String toPrint = "";
        for (int i = 0; i < counts.length; i++) {
            orig[i] -= counts[i];
            toPrint += orig[i] + " ";
        }
        System.out.println(toPrint);
    }

}
