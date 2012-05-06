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
package uk.co.kevinjjones.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * A stream provides a time series data abstraction. This is the read-write
 * version.
 */
public class RWStream implements ROStream {

    private final String _name;
    private final View _view;
    private final int _index;
    private String _description;
    private String _axis;
    private String _units;
    private ArrayList<Pair<String, String>> _meta = new ArrayList();
    private String[] _stringSet;

    protected RWStream(String name, String desc, String axis, String units, View view, int index) {
        _name = name;
        _description = desc;
        _axis = axis;
        _units = units;
        _view = view;
        _index = index;
    }

    public void setDescription(String description) {
        _description = description;
    }

    @Override
    public String name() {
        return _name;
    }

    @Override
    public String description() {
        return _description;
    }

    @Override
    public String axis() {
        return _axis;
    }

    @Override
    public String units() {
        return _units;
    }

    @Override
    public int size() {
        return _view.getDataLength(_index);
    }

    public void addData(String value) {
        _view.addData(_index, value);
    }

    @Override
    public String getString(int position) {
        return _view.getString(_index, position);
    }

    @Override
    public long getTick(int position) throws ParseException {
        return _view.getTick(_index, position);
    }

    @Override
    public double getNumeric(int position) throws NumberFormatException {
        return _view.getNumeric(_index, position);
    }

    @Override
    public String[] getStringSet() {
        if (_stringSet == null) {
            HashSet<String> s = new HashSet<String>();
            for (int i = 0; i < size(); i++) {
                s.add(getString(i));
            }
            _stringSet = (String[]) s.toArray(new String[0]);
            Arrays.sort(_stringSet);
        }
        return _stringSet;
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

    public void reverse() {
        _view.reverse(_index);
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
            i++;
        }
        return "";
    }
}
