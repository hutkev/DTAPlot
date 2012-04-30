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
import uk.co.kevinjjones.model.BasicError;
import uk.co.kevinjjones.model.ROStream;
import uk.co.kevinjjones.model.View;
import uk.co.kevinjjones.model.WithError;

/**
 * A stream provides a time series data abstraction. It implements some
 * basic statistical operations to make examining the stream easy.
 */
public class WheelStream extends StreamBase {
    
    public final static int LUSP=0;
    public final static int RUSP=1;
    public final static int LDSP=2;
    public final static int RDSP=3;
    
    public static String name(int index) {
        switch (index) {
            case LUSP:
                return "Left undriven wheel";
            case RUSP:
                return "Right undriven wheel";
            case LDSP:
                return "Left driven wheel";
            case RDSP:
                return "Right driven wheel";
        }
        return null;
    }
    
    private View _view;
    private boolean _init=false;
    private int _wheel=-1;
    private ROStream _stream;
    
    public WheelStream() {
    }
    
    @Override
    public void setView(View view, Object arg, WithError<Boolean,BasicError> ok) {
        _view=view;
        _wheel=((Integer)arg).intValue();
        assert(_wheel>=0 && _wheel<4);
        
        init();
        
        // Check for zero stream
        for (int i=0; i<size(); i++) {
            if (getNumeric(i)!=0)
                return;
        }
        
        ok.addError(new BasicError(BasicError.WARN,name()+ " trace is present but does did not record any values, ignoring it."));
        ok.setValue(false);    
    }
    
    @Override
    public String name() {
        return name(_wheel);
    }
    
    @Override    
    public String description() {
        return name();
    }
    
    @Override    
    public String axis() {
        return "Speed";
    }

    @Override
    public String units() {
        return null; // Unknown
    }
    
    @Override
    public int size() {
        return _view.getStream(Log.TIME_STREAM).size();
    }
    
    private void init() {
        if (_init)
            return;
        
        switch (_wheel) {
            case LUSP:
                _stream=_view.getStream(Log.LUSP_STREAM);
                break;
            case RUSP:
                _stream=_view.getStream(Log.RUSP_STREAM);
                break;
            case LDSP:
                _stream=_view.getStream(Log.LDSP_STREAM);
                break;
            case RDSP:
                _stream=_view.getStream(Log.RDSP_STREAM);
                break;
        }
        _stream.setMeta("hide", "true");
        _init=true;
    }
    
    @Override
    public void validate(WithError<Boolean,BasicError> ok) {
        init();
    }
    
    @Override
    public double getNumeric(int position) throws NumberFormatException {
        init();
        return _stream.getNumeric(position);
    }
}
