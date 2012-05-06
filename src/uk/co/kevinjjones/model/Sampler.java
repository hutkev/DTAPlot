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
 * Base sampler class.
 * Samplers count collections of values into buckets
 */
public abstract class Sampler<T> {

    public class SamplerRangeError extends ModelError<Sampler<T>> {

        private int _index;
        private T _value;

        public SamplerRangeError(Sampler<T> sampler, int index, T value) {
            super(sampler);
            _index = index;
            _value = value;
        }

        public int index() {
            return _index;
        }

        public T value() {
            return _value;
        }
    }

    public abstract T min();

    public abstract T max();

    public abstract int buckets();

    public abstract WithError<ArrayList<Integer>, SamplerRangeError> sample(T[] value);
}
