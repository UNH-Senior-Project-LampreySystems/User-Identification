/**
 * ButtonPressReiver.java - class that is to be run by a thread and listens for a Dash button press
 * @author John Peck
 * @version 1.0
 */
package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ButtonPressReceiver implements Runnable {
    private Thread t;
    private String threadName;
    private boolean done;

    /**
     * ButtonPresssReceiver = constructor
     * @param name - takes a string name to assign as the thread name
     */
    ButtonPressReceiver(String name) {
        this.threadName = name;
        this.done = false;
    }

    /**
     * run - executes listForButtonPress method
     */
    public void run() {
        this.listenForButtonPress();
    }

    /**
     * start - creates and starts thread
     */
    public void start() {
        if(this.t == null) {
            this.t = new Thread(this, this.threadName);
            this.t.start();
        }

    }

    /**
     * isDone - accessor for done boolean
     * @return - returns done boolean
     */
    public boolean isDone() {
        return this.done;
    }

    /**
     * stop - stops thread
     */
    public void stop() {
        this.done = true;
        this.t.stop();
    }

    /**
     * listenForButtonPress - executes parse for probe and continualy check to see if the button press has been found
     * yet, stop the thread when it has been found
     */
    public void listenForButtonPress() {
        try {
            PrintWriter e = new PrintWriter("out.txt");
            e.print("");
            e.close();

            for(boolean done = false; !done; done = parseForProbe()) {
                ;
            }
        } catch (FileNotFoundException var3) {
            var3.printStackTrace();
        }

        this.stop();
    }

    /**
     * parseForProbe - reads the output file for the dash_list.py script and parses it for the mac address of the glad
     * dash button
     * @return - returns true if the glad dash button mac address has been found
     */
    public static boolean parseForProbe() {
        try {
            BufferedReader ioe = new BufferedReader(new FileReader("/home/pi/Development/out.txt"));
            Throwable var1 = null;

            boolean var3 = false;
            try {
                String line;
                do {
                    if((line = ioe.readLine()) == null) {
                        ioe.close();
                        return false;
                    }
                } while(!line.contains("74c246d733c4"));

                var3 = true;
            } catch (Throwable var15) {
                var1 = var15;
                var15.printStackTrace();
            } finally {
                if(ioe != null) {
                    if(var1 != null) {
                        try {
                            ioe.close();
                        } catch (Throwable var14) {
                            var14.printStackTrace();
                        }
                    } else {
                        ioe.close();
                    }
                }

            }

            return var3;
        } catch (FileNotFoundException var17) {
            System.out.println("Could not open file out.txt");
            var17.printStackTrace();
        } catch (IOException var18) {
            System.out.println("Got IOException when trying to read file out.txt");
            var18.printStackTrace();
        }

        return false;
    }
}
