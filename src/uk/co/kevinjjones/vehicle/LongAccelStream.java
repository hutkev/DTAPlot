/**
 * Copyright 2012 Kevin J. Jones (http://www.kevinjjones.co.uk)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package uk.co.kevinjjones.vehicle;

import java.util.ArrayList;
import uk.co.kevinjjones.model.*;

/**
 * Longitudinal acceleration virtual stream.
 */
public class LongAccelStream extends StreamBase {

    private View _view;
    private ROStream _speed;
    private ArrayList<Double> _accel = new ArrayList();

    public LongAccelStream() {
    }

    @Override
    public void setView(View view, Object arg, ParamHandler handler, WithError<Boolean, BasicError> ok) {
        super.setView(view, arg, handler, ok);
        _view = view;

        // Must have some speed measure
        _speed = _view.getStream(SpeedStream.NAME);
        if (_speed == null) {
            ok.setValue(false);
            return;
        }

        for (int i = 0; i < _speed.size(); i++) {

            int minpos = i;
            if (minpos > 0) {
                minpos--;
            }
            if (minpos > 0) {
                minpos--;
            }
            int maxpos = i;
            if (maxpos + 1 < _speed.size()) {
                maxpos++;
            }
            if (maxpos + 1 < _speed.size()) {
                maxpos++;
            }


            double minSpeed = _speed.getNumeric(minpos) * 1000 / 3600;
            double maxSpeed = _speed.getNumeric(maxpos) * 1000 / 3600;

            double accel = (maxSpeed - minSpeed) / (0.1 * (maxpos - minpos)) / 9.806;
            _accel.add(accel);
        }
    }

    @Override
    public String name() {
        return "Longitudinal Accel.";
    }

    @Override
    public String description() {
        return name();
    }

    @Override
    public String axis() {
        return "Acceleration";
    }

    @Override
    public String units() {
        return "g"; // Unknown
    }

    @Override
    public int size() {
        return _accel.size();
    }

    @Override
    public double getNumeric(int position) throws NumberFormatException {
        switch (isKPH(true)) {
            case 1:
                return _accel.get(position);
            case 2:
                return _accel.get(position) / 1.609;
        }
        return 0;
    }
}
