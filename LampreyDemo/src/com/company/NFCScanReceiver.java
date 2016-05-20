/**
 * NFCScanReceiver - class that is to be run on a thread and listens for a NFC swipe
 * @author John Peck
 * @version 1.0
 */
package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by jeep on 5/10/16.
 */
public class NFCScanReceiver implements Runnable {
    private Thread t;
    private String threadName;
    private boolean done;

    /**
     * NFCScanReceiver - constuctor
     * @param name - takes a string to assign as the name of the thread
     */
    NFCScanReceiver(String name) {
        this.threadName = name;
        this.done = false;
    }

    /**
     * run - exectues the listenForScan method
     */
    public void run() {
        this.listenForNFCScan();
    }

    /**
     * start - creates and starts the thread
     */
    public void start() {
        if(this.t == null) {
            this.t = new Thread(this, this.threadName);
            this.t.start();
        }

    }

    /**
     * stop - stops the thread
     */
    public void stop() {
        this.done = true;
        this.t.stop();
    }

    /**
     * isDone - accessor for done boolean
     * @return - returns done boolean
     */
    public boolean isDone() {
        return this.done;
    }

    /**
     * listenForNFCScan - executes nfc-poll command and then parse the output for the UUID of the nfc keychain, when it
     * is received it stops the thread.
     */
    public void listenForNFCScan() {
        String cmd = "nfc-poll";

        try {
            Process e = Runtime.getRuntime().exec(cmd);
            BufferedReader input = new BufferedReader(new InputStreamReader(e.getInputStream()));

            String line;
            while((line = input.readLine()) != null && !line.contains("80  18  c6  de")) {
                ;
            }
            input.close();
            this.stop();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }
}
