/**
 * BarcodeScanReceiver - class to be run of thread that listens for a barcode scan
 * @author John Peck
 * @version 1.0
 */
package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BarcodeScanReceiver implements Runnable {
    private Thread t;
    private String threadName;
    private boolean done;

    /**
     * BarcodeScanReceiver - constructor
     * @param name - take a string to assign as the name of the thread
     */
    BarcodeScanReceiver(String name) {
        this.threadName = name;
        this.done = false;
    }

    /**
     * run - exectues the listenForBarcodeScan method
     */
    public void run() {
        this.listenForBarcodeScan();
    }

    /**
     * start - starts and creates the thread
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
     * listenForBarcodeScan - cats the /dev/rfcomm0 file which is what the barcode scanner is bound too, so any barcodes
     * that are scanned will be outputted here, it parses the output for a specific barcode and then stops the thread when
     * is is received
     */
    public void listenForBarcodeScan() {
        String cmd = "cat /dev/rfcomm0";

        try {
            Process e = Runtime.getRuntime().exec(cmd);
            BufferedReader input = new BufferedReader(new InputStreamReader(e.getInputStream()));

            String line;
            while((line = input.readLine()) != null && !line.contains("BY8")) {
                ;
            }

            input.close();
            this.stop();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }
}