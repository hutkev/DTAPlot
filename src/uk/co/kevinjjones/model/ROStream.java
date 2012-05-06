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

/**
 * A stream provides a time series data abstraction. This is a read-only
 * version.
 */
public interface ROStream {

    public void setMeta(String id, String value);

    public String getMeta(String id);

    public String name();

    public String description();

    public String axis();

    public String units();

    public int size();

    public String getString(int position);

    public long getTick(int position) throws ParseException;

    public double getNumeric(int position) throws NumberFormatException;

    public String[] getStringSet();

    public Double[] toArray() throws NumberFormatException;
}
