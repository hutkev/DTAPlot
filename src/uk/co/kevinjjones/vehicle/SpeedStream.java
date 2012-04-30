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
public class SpeedStream extends StreamBase {
    
    public static String NAME="Speed";
    
    private View _view;
    private ROStream _lusp;
    private ROStream _rusp;
    private ROStream _ldsp;
    private ROStream _rdsp;
    private ROStream _lowt;
    
    public SpeedStream() {
    }
    
    @Override
    public void setView(View view, Object arg, WithError<Boolean,BasicError> ok) {
        _view=view;
        _lusp=_view.getStream(WheelStream.name(WheelStream.LUSP));
        _rusp=_view.getStream(WheelStream.name(WheelStream.RUSP));
        _ldsp=_view.getStream(WheelStream.name(WheelStream.LDSP));
        _rdsp=_view.getStream(WheelStream.name(WheelStream.RDSP));
        _lowt=_view.getStream(LowThrottle.NAME);
        
        if (_lusp!=null && _rusp!=null && _ldsp!=null && _rdsp!=null) {
            ok.addError(new BasicError(BasicError.WARN,"Using four wheel speeds to calculate speed"));
        } else if (_lusp!=null && _rusp!=null) {
            ok.addError(new BasicError(BasicError.WARN,"Using two undriven wheel speeds to calculate speed"));
        } else if (_lusp!=null) {
            ok.addError(new BasicError(BasicError.WARN,"Using left undriven wheel speeds to calculate speed"));
        } else if (_rusp!=null) {
            ok.addError(new BasicError(BasicError.WARN,"Using right undriven wheel speeds to calculate speed"));
        } else if (_ldsp!=null && _rdsp!=null) {
            ok.addError(new BasicError(BasicError.WARN,"Using two driven wheel speeds to calculate speed"));
        } else if (_ldsp!=null) {
            ok.addError(new BasicError(BasicError.WARN,"Using left driven wheel speeds to calculate speed"));
        } else if (_rdsp!=null) {
            ok.addError(new BasicError(BasicError.WARN,"Using right driven wheel speeds to calculate speed"));
        } else {
            ok.addError(new BasicError(BasicError.WARN,"No wheel speeds available"));
            ok.setValue(false);
        }
    }
    
    @Override
    public String name() {
        return NAME;
    }
    
    @Override    
    public String description() {
        return "Speed";
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
    
    @Override
    public double getNumeric(int position) throws NumberFormatException {
        
        if (_lusp!=null && _rusp!=null && _ldsp!=null && _rdsp!=null) {
            // With four wheel sensors we track min under accel
            // and max under braking, the switch over is made by looking
            // at tps
            double lowt=_lowt.getNumeric(position);
            double avgDriven=(_ldsp.getNumeric(position)+_rdsp.getNumeric(position))/2;
            double avgUndriven=(_lusp.getNumeric(position)+_rusp.getNumeric(position))/2;
            
            if (lowt==1) {
                return Math.max(avgDriven,avgUndriven);
            } else {
                return Math.min(avgDriven,avgUndriven);
            }
        }
        else if (_lusp!=null && _rusp!=null) {
            return (_lusp.getNumeric(position)+_rusp.getNumeric(position))/2;
        } else if (_lusp!=null) {
            return _lusp.getNumeric(position);
        } else if (_rusp!=null) {
            return _rusp.getNumeric(position);            
        } else if (_ldsp!=null && _rdsp!=null) {
            return (_ldsp.getNumeric(position)+_rdsp.getNumeric(position))/2;
        } else if (_ldsp!=null) {
            return _ldsp.getNumeric(position);
        } else if (_rdsp!=null) {
            return _rdsp.getNumeric(position);            
        }
        return 0;
    }
}
