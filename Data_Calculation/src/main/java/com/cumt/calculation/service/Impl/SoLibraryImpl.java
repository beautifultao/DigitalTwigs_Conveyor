package com.cumt.calculation.service.Impl;

import com.cumt.calculation.entity.CoalFlow;
import com.cumt.calculation.entity.Vector3;
import com.cumt.calculation.service.ISoLibrary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SoLibraryImpl implements ISoLibrary {
    @Override
    public List<Vector3> getPoints(byte[] rawPointData) {
        /*
           反序列过程
         */
        return null;
    }

    @Override
    public CoalFlow getCoalFlow(byte[] rawData) {
        /*
         *  反序列过程
         */
        return ISoLibrary.INSTANCE.getCoalFlow(rawData);
    }
}
