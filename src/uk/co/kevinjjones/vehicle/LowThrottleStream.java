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

import uk.co.kevinjjones.Log;
import uk.co.kevinjjones.model.*;

/**
 * Low throttle virtual stream.
 */
public class LowThrottleStream extends StreamBase {

    public static String NAME = "Low Throttle";
    private View _view;
    private ROStream _tps;

    public LowThrottleStream() {
    }

    @Override
    public void setView(View view, Object arg, ParamHandler handler, WithError<Boolean, BasicError> ok) {
        _view = view;
        _tps = _view.getStream(Log.THROT_STREAM);

        if (_tps == null) {
            ok.setValue(false);
        }

    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public String description() {
        return "Throttle < 5%";
    }

    @Override
    public String axis() {
        return "Low Throttle";
    }

    @Override
    public String units() {
        return null; // Unknown
    }

    @Override
    public int size() {
        return _tps.size();
    }

    @Override
    public double getNumeric(int position) throws NumberFormatException {

        if (_tps.getNumeric(position) < 5) {
            return 1;
        } else {
            return 0;
        }
    }
}
