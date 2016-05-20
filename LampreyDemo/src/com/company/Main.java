/**
 * Main.java - main class of LampreyDemo, listens for a "measurement" to be received, once it is it spawns a thread for
 * that is listening for each of the different identification methods
 * @author John Peck
 * @version 1.0
 */
package com.company;

import java.io.*;

public class Main {

    public static boolean started = false;

    /**
     * listenForMeasurement - listen's for a smart water dash button press
     * @return - returns true when a "measurement" is received
     */
    public static boolean listenForMeasurement() {
        boolean ret = false;
        System.out.println("\nWaiting for a measurement");
        if(!started) {
            started = true;
            try {
                boolean e = false;

                while(!e) {
                    System.out.print(".");
                    Process done = Runtime.getRuntime().exec("sudo python dash_sniff2.py &");
                    BufferedReader ie = new BufferedReader(new InputStreamReader(done.getInputStream()));
                    Thread.sleep(1000L);
                    ie.close();
                    done.destroy();
                    e = parseForProbe();
                    if(e) {
                        return true;
                    }
                }
            } catch (Exception var8) {
                System.out.println("Could not execute command #:(");
            }
        } else {
            try {
                PrintWriter e1 = new PrintWriter("out.txt");
                e1.print("");
                e1.close();
                boolean done1 = false;

                while(!done1) {
                    try {
                        System.out.print(".");
                        done1 = parseForProbe();
                        if(done1) {
                            return true;
                        }

                        Thread.sleep(1000L);
                    } catch (InterruptedException var6) {
                        System.out.println("Got an interrupt exception");
                    }
                }
            } catch (FileNotFoundException var7) {
                var7.printStackTrace();
            }
        }

        return ret;
    }

    /**
     * parseForProbe - The script for that sniffs arp probes writes out to a file so this method parses the file for
     * the mac address matching the smart water dash button
     * @return - return true when it finds the mac address of the smart water button
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
                } while(!line.contains("a002dc086524"));

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

    /**
     * launchThreads - starts all the threads to listen for the different identification signals
     * @return - returns true when an identification has been received
     */
    public static boolean launchThreads() {
        boolean done = false;
        System.out.println("\nMeasurement received, Listening For User Identification");
        ButtonPressReceiver buttonPressReceiver = new ButtonPressReceiver("ButtonPressReceiver");
        buttonPressReceiver.start();
        BarcodeScanReceiver barcodeScanReceiver = new BarcodeScanReceiver("BarcodeScanReceiver");
        barcodeScanReceiver.start();

        NFCScanReceiver nfcScanReceiver = new NFCScanReceiver("NFCScanReceiver");
        nfcScanReceiver.start();
        while(!done) {
            try {
                System.out.print(".");
                if(buttonPressReceiver.isDone()) {
                    System.out.println("\nGot a button press from User-1(Nick)");
                    barcodeScanReceiver.stop();
                    done = true;
                } else if(barcodeScanReceiver.isDone()) {
                    System.out.println("\nGot a barcode scan from User-2(Joel)");
                    buttonPressReceiver.stop();
                    done = true;
                } else if(nfcScanReceiver.isDone()){
                    System.out.println("\nGot a NFC swipe from User-3(John)");
                    nfcScanReceiver.stop();
                    done = true;
                }

                Thread.sleep(1000L);
            } catch (InterruptedException var4) {
                var4.printStackTrace();
            }
        }

        return done;
    }

    /**
     * main - just the main method, run a loop that listen for a mesurement and once one
     * is received starts listening for idenitifcation
     * @param args
     */
    public static void main(String[] args) {
        boolean getOut = false;

        while(!getOut) {
            boolean listen = listenForMeasurement();
            if(listen) {
                boolean test = launchThreads();
                if(!test) {
                    getOut = true;
                }
            } else {
                System.out.println("Never got a measurement");
                getOut = true;
            }
        }

    }
}
