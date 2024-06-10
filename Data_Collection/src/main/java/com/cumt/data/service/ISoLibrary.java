package com.cumt.data.service;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface ISoLibrary extends Library {
    ISoLibrary INSTANCE = Native.load("Collection", ISoLibrary.class);

    byte[] getPoints();
    byte[] getPLCData();
    boolean startSocket();
    boolean stopSocket();
    boolean isConnected();
}
