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
 * Error passing handler
 */
public class WithError<T, E> {

    private T _value;
    private ArrayList<E> _errors = new ArrayList();

    public WithError(T value) {
        _value = value;
    }

    public T value() {
        return _value;
    }

    public void setValue(T value) {
        _value = value;
    }

    public boolean hasErrors() {
        return (_errors != null);
    }

    public ArrayList<E> errors() {
        return _errors;
    }

    public void addError(E e) {
        _errors.add(e);
    }

    public void appendErrors(WithError<T, E> we) {
        for (E e : we._errors) {
            _errors.add(e);
        }
    }
}
