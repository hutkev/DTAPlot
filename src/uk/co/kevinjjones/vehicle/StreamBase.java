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

import java.text.ParseException;
import java.util.ArrayList;
import uk.co.kevinjjones.model.*;

/**
 * Base class for virtual streams.
 */
public abstract class StreamBase implements VStream {

    private ParamHandler _handler;
    private ArrayList<Pair<String, String>> _meta = new ArrayList();
    private int _isKPH = 3;

    public StreamBase() {
    }

    @Override
    public void setView(View view, Object arg, ParamHandler handler, WithError<Boolean, BasicError> ok) {
        _handler = handler;
    }

    public int isKPH(boolean prompt) {
        if (_isKPH == 3) {
            String v = _handler.getParameter("isKPH", prompt);
            if (v == null) {
                _isKPH = 0;
            } else if (v.equals("true")) {
                _isKPH = 1;
            } else if (v.equals("false")) {
                _isKPH = 2;
            }
        }
        return _isKPH;
    }

    @Override
    public double getNumeric(int position) throws NumberFormatException {
        assert (false);
        return 0;
    }

    @Override
    public String getString(int position) {
        assert (false);
        return null;
    }

    @Override
    public long getTick(int position) throws ParseException {
        assert (false);
        return 0;
    }

    @Override
    public String[] getStringSet() {
        assert (false);
        return null;
    }

    @Override
    public Double[] toArray() throws NumberFormatException {
        int s = size();
        Double[] da = new Double[s];
        for (int i = 0; i < s; i++) {
            da[i] = new Double(getNumeric(i));
        }
        return da;
    }

    @Override
    public void setMeta(String id, String value) {
        _meta.add(new Pair(id, value));
    }

    @Override
    public String getMeta(String id) {
        int i = 0;
        while (i < _meta.size()) {
            if (_meta.get(i).first().equals(id)) {
                return _meta.get(i).second();
            }
        }
        return "";
    }
}
