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

/**
 * The missing Pair class
 */
public class Pair<X, Y> {

    private X _x;
    private Y _y;

    public Pair(X x, Y y) {
        _x = x;
        _y = y;
    }

    public X first() {
        return _x;
    }

    public Y second() {
        return _y;
    }

    public void setFirst(X x) {
        _x = x;
    }

    public void setSecond(Y y) {
        _y = y;
    }
}
