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

import java.util.ArrayList;

/**
 * Distribution analysis assistant
 */
public class Distribution<T> {

    private Sampler<T> _sampler;
    ArrayList<Pair<Integer, Integer>> _errorRanges;
    private ArrayList<Sampler<T>.SamplerRangeError> _errors = new ArrayList();
    private int[] _count;

    public Distribution(Sampler<T> sampler) {
        _sampler = sampler;
        _count = new int[sampler.buckets()];
    }

    public void sample(T[] values) {
        WithError<ArrayList<Integer>, Sampler<T>.SamplerRangeError> c = _sampler.sample(values);
        _errors.addAll(c.errors());
        ArrayList<Integer> v = c.value();

        assert (v.size() == _count.length);
        for (int i = 0; i < _count.length; i++) {
            _count[i] += v.get(i);
        }
        _errorRanges = null;
    }

    public int samples(int slot) {
        return _count[slot];
    }

    public ArrayList<Pair<Integer, Integer>> errors() {
        if (_errorRanges == null) {
            _errorRanges = new ArrayList();

            // Naive linear clustering
            int idx = 0;
            while (idx < _errors.size()) {
                Pair<Integer, Integer> r = new Pair(_errors.get(idx), _errors.get(idx));
                _errorRanges.add(r);
                idx++;

                while (idx < _errors.size()) {
                    if (_errors.get(idx).index() == r.second() + 1) {
                        r.setSecond(r.second() + 1);
                        idx++;
                    } else {
                        break;
                    }
                }
            }
        }
        return _errorRanges;
    }
}
