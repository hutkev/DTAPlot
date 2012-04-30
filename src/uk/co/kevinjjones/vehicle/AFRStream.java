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
public class AFRStream extends StreamBase {
    
    public final static int AFR=1;
    public final static int LAMBDA=2;
    
    public final static String AFR_NAME="AFR";
    public final static String LAMBDA_NAME="LAMBDA";
        
    public static String name(int index) {
        switch (index) {
            case AFR:
                return AFR_NAME;
            case LAMBDA:
                return LAMBDA_NAME;
        }
        return null;
    }
    
    private static int _isAFR=0;
    
    private static boolean isAFR(ROStream s) {
        if (_isAFR!=0)
            return _isAFR==AFR;
        
        DoubleSampler ds=new DoubleSampler(0.0,20.0,200);
        Distribution<Double> d=new Distribution<Double>(ds);
        d.sample(s.toArray());
        
        // If Lambda we should not see samples >2
        int b=20;
        for(; b<200; b++) {
            if (d.samples(b)>0)
                break;
        }
        if (b>=200) {
            _isAFR=LAMBDA;
        } else {
            _isAFR=AFR;
        }
        
        return _isAFR==1;
    }
    
    private View _view;
    private int _type=-1;
    private ROStream _stream;
    private int _streamType;
    
    public AFRStream() {
    }
    
    @Override
    public void setView(View view, Object arg, WithError<Boolean,BasicError> ok) {
        _view=view;
        _type=((Integer)arg).intValue();
        assert(_type>=1 && _type<=2);
        
        _stream=_view.getStream(Log.LAMB_STREAM);
        if (_stream==null) {
            ok.setValue(false);
        } else {
            _stream.setMeta("hide", "true");
            _streamType=isAFR(_stream)?AFR:LAMBDA;
        }
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
        return name(_type);
    }

    @Override
    public String units() {
        return null; // Unknown
    }
    
    @Override
    public int size() {
        return _view.getStream(Log.TIME_STREAM).size();
    }
    
    @Override
    public void validate(WithError<Boolean,BasicError> ok) {
    }
    
    @Override
    public double getNumeric(int position) throws NumberFormatException {
        double v=_stream.getNumeric(position);
        if (_type==AFR && _streamType!=AFR) {
            return v*14.7;
        } else if (_type==LAMBDA && _streamType!=LAMBDA) {
            return v/14.7;
        }
        return v;
    }
}
