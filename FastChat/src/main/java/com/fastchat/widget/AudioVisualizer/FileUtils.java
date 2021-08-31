package com.fastchat.widget.AudioVisualizer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileUtils {
    public static byte[] fileToBytes(File file) {
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static int[] createWaveform(File file) {
        byte[] f=fileToBytes(file);
         final int[] values = new int[f.length];
        int maxValue = 0;

        for (int i = 0; i < f.length; i++) {
            final int newValue = 5 + f.length;
            if (newValue > maxValue) {
                maxValue = newValue;
            }
            values[i] = newValue;
        }
        return values;
    }
}