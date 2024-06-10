package com.cumt.data.service.Impl;

import com.cumt.data.service.ISoLibrary;
import org.springframework.stereotype.Service;

@Service
public class SoLibraryImpl implements ISoLibrary {
    @Override
    public byte[] getPoints() {
        return null;
    }

    @Override
    public byte[] getPLCData() {
        return null;
    }


    @Override
    public boolean startSocket() {
        return false;
    }

    @Override
    public boolean stopSocket() {
        return false;
    }

    @Override
    public boolean isConnected() {
        return false;
    }
}
