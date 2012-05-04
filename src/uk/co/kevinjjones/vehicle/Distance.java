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

import java.util.ArrayList;
import uk.co.kevinjjones.Log;
import uk.co.kevinjjones.model.*;

/**
 * A stream provides a time series data abstraction. It implements some
 * basic statistical operations to make examining the stream easy.
 */
public class Distance extends StreamBase {
    
    private View _view;
    private ROStream _speed;
    private ParamHandler _handler;
    private ArrayList<Double> _distance=new ArrayList();
    
    public Distance() {
    }
    
    @Override
    public void setView(View view, Object arg, ParamHandler handler, WithError<Boolean,BasicError> ok) {
        super.setView(view, arg, handler, ok);
        _view=view;
        _handler=handler;
        _speed=_view.getStream(SpeedStream.NAME);
        if (_speed==null) 
            ok.setValue(false);
    }
    
    @Override
    public String name() {
        return "Distance";
    }
    
    @Override    
    public String description() {
        return "Distance";
    }
    
    @Override    
    public String axis() {
        return "Distance";
    }

    @Override
    public String units() {
        return "Meters";
    }
    
    @Override
    public int size() {
        return _view.getStream(Log.TIME_STREAM).size();
    }
    
    
    @Override
    public double getNumeric(int position) throws NumberFormatException {
        
        while (_distance.size()<position+1) {
            double speed=_speed.getNumeric(_distance.size());
            switch (isKPH(true)) {
                case 0:
                    speed=0;    // No declared unit
                    break;
                case 2:         // In miles
                    speed=speed*1.609;
            }
            
            if (_distance.isEmpty())
                _distance.add((speed*1000)/60/60/10);
            else
                _distance.add(_distance.get(_distance.size()-1)+(speed*1000)/60/60/10);
        }
        return _distance.get(position);
    }
}
