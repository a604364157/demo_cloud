package com.jjx.cloudnacosprovider.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author jiangjx
 */
@SuppressWarnings("unused")
public class MusicUtil {

    private static void changeUcToMp3(String ucName, String mpgName) {
        File inFile = new File(ucName);
        File outFile = new File(mpgName);
        try (DataInputStream dis = new DataInputStream(new FileInputStream(inFile));
             DataOutputStream dos = new DataOutputStream(new FileOutputStream(outFile))) {
            byte[] b = new byte[1024];
            int len;
            while ((len = dis.read(b)) != -1) {
                for (int i = 0; i < len; i++) {
                    b[i] ^= 0xa3;
                }
                dos.write(b, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}