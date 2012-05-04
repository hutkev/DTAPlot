/**
Copyright 2011 Kevin J. Jones (http://www.kevinjjones.co.uk)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package uk.co.kevinjjones.vehicle;

import uk.co.kevinjjones.Log;
import uk.co.kevinjjones.model.*;

/**
 * A stream provides a time series data abstraction. It implements some
 * basic statistical operations to make examining the stream easy.
 */
public class TempStream extends StreamBase {
    
    public static int DEGC=1;
    public static int DEGF=2;
    
    private static int _isDegC=0;
    
    private static boolean isDegC(ROStream s) {
        if (_isDegC!=0)
            return _isDegC==DEGC;
        
        DoubleSampler ds=new DoubleSampler(-100.0,500.0,12);
        Distribution<Double> d=new Distribution<Double>(ds);
        d.sample(s.toArray());
        
        // If DegC we should not see > 150
        int b=5;
        for(; b<12; b++) {
            if (d.samples(b)>0)
                break;
        }
        if (b>=12) {
            _isDegC=DEGC;
        } else {
            _isDegC=DEGF;
        }
        
        return _isDegC==DEGC;
    }
    
    public final static int WATER=1;
    public final static int OIL=2;
    public final static int AIR=3;
    
    public final static String WATER_NAME="Water Temp";
    public final static String OIL_NAME="Oil Temp";
    public final static String AIR_NAME="Air Temp";
        
    public static String name(int index) {
        switch (index) {
            case WATER:
                return WATER_NAME;
            case OIL:
                return OIL_NAME;
            case AIR:
                return AIR_NAME;
                
        }
        return null;
    }
    
    private View _view;
    private int _type=-1;
    private ROStream _stream;
    
    public TempStream() {
    }
    
    @Override
    public void setView(View view, Object arg, ParamHandler handler, WithError<Boolean,BasicError> ok) {
        _view=view;
        _type=((Integer)arg).intValue();
        assert(_type>=1 && _type<=3);
        
        switch (_type) {
            case WATER:
                _stream=_view.getStream(Log.WATER_STREAM);
                break;
            case OIL:
                _stream=_view.getStream(Log.OILT_STREAM);
                break;
            case AIR:
                _stream=_view.getStream(Log.AIR_STREAM);
                break;
        }
        
        if (_stream==null) {
            ok.setValue(false);
        } else {
            _stream.setMeta("hide", "true");
        }
        isDegC(_stream);
    }
    
    @Override
    public String name() {
        return name(_type);
    }
    
    @Override    
    public String description() {
        return name();
    }
    
    @Override    
    public String axis() {
        return "Temperature";
    }

    @Override
    public String units() {
        if (isDegC(_stream)) {
            return "\u00B0C";
        } else {
            return "\u00B0F";
        }
    }
    
    @Override
    public int size() {
        return _view.getStream(Log.TIME_STREAM).size();
    }
    
    @Override
    public double getNumeric(int position) throws NumberFormatException {
        return _stream.getNumeric(position);
    }
    
}
