package com.cumt.data.service;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface ICollectionSo extends Library {
    ICollectionSo INSTANCE = Native.load("Collection", ICollectionSo.class);

    byte[] getPoints();
    byte[] getPLCData();
    boolean startSocket();
    boolean stopSocket();
    boolean isConnected();
}
