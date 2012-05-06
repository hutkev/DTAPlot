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
 * Simple error holder
 */
public class BasicError {

    public static int INFO = 0;
    public static int WARN = 1;
    public static int ERROR = 2;
    public int _type;
    public String _msg;
    public Exception _ex;
    public View _log;
    public String _stream;
    public int _start;
    public int _end;

    public BasicError(int type, String msg, Exception e) {
        _type = type;
        _msg = msg;
        _ex = e;
    }

    public BasicError(int type, String msg) {
        _type = type;
        _msg = msg;
    }

    public BasicError(int type, Exception e) {
        _type = type;
        _ex = e;
    }

    public BasicError(String msg) {
        _type = ERROR;
        _msg = msg;
    }

    public BasicError(String msg, Exception e) {
        _type = ERROR;
        _msg = msg;
        _ex = e;
    }

    public BasicError(Exception e) {
        _type = ERROR;
        _ex = e;
    }
}
