/**
 Copyright 2011 Kevin J. Jones (http://www.kevinjjones.co.uk)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package uk.co.kevinjjones;

import au.com.bytecode.opencsv.CSVReader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Log {

    private String _name;
    private Object[] _rows = null;
    private double[] _leftSpeed = null;
    private double[] _rightSpeed = null;
    private double[] _distance = null;
    private int _leftSpeedCol;
    private int _rightSpeedCol;
    private int _rpmCol;
    private int _tpsCol;
    private int _mapCol;
    private int _lambdaCol;
    private int _waterCol;
    private int _airTCol;
    private int _oilTCol;
    private int _oilPCol;
    private int _slipCol;
    private int _turboCol;
    private int _boostCol;
    private boolean _degC = true;
    private boolean _KPA = true;
    private boolean _KPH = true;
    private boolean _lambda = true;
    private boolean _hasRightSpeed = false;

    static byte[] readFile(FileInputStream fis) throws Exception {
        InputStream in = null;
        byte[] out = new byte[0];

        try {
            in = new BufferedInputStream(fis);

            // the length of a buffer can vary
            int bufLen = 20000 * 1024;
            byte[] buf = new byte[bufLen];
            byte[] tmp = null;
            int len = 0;
            while ((len = in.read(buf, 0, bufLen)) > 0) {
                // extend array
                tmp = new byte[out.length + len];

                // copy data
                System.arraycopy(out, 0, tmp, 0, out.length);
                System.arraycopy(buf, 0, tmp, out.length, len);

                out = tmp;
                tmp = null;
            }

        } finally {
            // always close the stream
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
        return out;
    }

    public class LogException extends RTException {

        public LogException(String error) {
            super(error);
        }

        public LogException(String error, Exception e) {
            super(error, e);
        }
    }

    public Log(File f, boolean degC, boolean KPA, boolean KPH, boolean lambda) throws LogException {
        _degC=degC;
        _KPA=KPA;
        _KPH=KPH;
        _lambda=lambda;
        loadLogfile(f);
    }

    public final void loadLogfile(File f) throws LogException {

        // Check its readable
        if (!f.canRead()) {
            throw new LogException("The logfile " + f.getAbsolutePath() 
                    + " can not be read.");
        }

        // Now read the data stream
        FileInputStream fis;
        byte[] data = null;
        try {
            fis = new FileInputStream(f);
            data = readFile(fis);
        } catch (Exception e) {
            throw new LogException("An error occured reading the logfile " 
                    + f.getAbsolutePath(), e);
        }
        _name=f.getName();

        CSVReader parser = new CSVReader(new StringReader(new String(data)), ';');
        try {
            // Grab all the data in one go
            _rows = parser.readAll().toArray();
        } catch (IOException e) {
            throw new LogException("Problem loading data in logfile " + 
                    f.getAbsolutePath(), e);
        }

        // Now locate columns
        _leftSpeedCol=-1;
        _rightSpeedCol=-1;
        _rpmCol=-1;
        _tpsCol=-1;
        _mapCol=-1;
        _lambdaCol=-1;
        _waterCol=-1;
        _airTCol=-1;
        _oilTCol=-1;
        _oilPCol=-1;
        _slipCol=-1;
        _turboCol=-1;
        _boostCol=-1;

        String[] nextLine = (String[]) _rows[0];
        if (nextLine != null) {
            int NUMindex = nextLine.length;
            for (int i = 0; i < NUMindex; i++) {
                if (nextLine[i].equals("L U SP")) {
                    _leftSpeedCol = i;
                }
                else if (nextLine[i].equals("R U SP")) {
                    _rightSpeedCol = i;
                }
                else if (nextLine[i].equals("RPM")) {
                    _rpmCol = i;
                }
                else if (nextLine[i].equals("THROT")) {
                    _tpsCol = i;
                }
                else if (nextLine[i].equals("MAP")) {
                    _mapCol = i;
                }
                else if (nextLine[i].equals("LAMB")) {
                    _lambdaCol = i;
                }
                else if (nextLine[i].equals("WATER")) {
                    _waterCol = i;
                }
                else if (nextLine[i].equals("AIR")) {
                    _airTCol = i;
                }
                else if (nextLine[i].equals("OIL T")) {
                    _oilTCol = i;
                }
                else if (nextLine[i].equals("OIL P")) {
                    _oilPCol = i;
                }
                else if (nextLine[i].equals("SLIP%")) {
                    _slipCol = i;
                }
                else if (nextLine[i].equals("TURB %")) {
                    _turboCol = i;
                }
                else if (nextLine[i].equals("A1 VAL")) {
                    _boostCol = i;
                }
            }
            
            // Check we at least have speed
            if (_leftSpeedCol == -1 && _rightSpeedCol==-1) {
                throw new LogException("No speed column (L U SP or R U SP) found in "+
                        f.getAbsolutePath()+", maybe this is not a logfile");
            }
        } else {
            throw new LogException("Missing header row in "+
                    f.getAbsolutePath()+", perhaps an empty file");
        }
        
        // First two rows are now junk, and log needs reversing
        List<Object> reverse = Arrays.asList(_rows);
        Collections.reverse(reverse);
        _rows=reverse.toArray();
       
        // Finally cache speed & calc distance column
        if (_leftSpeedCol!=-1)
            _leftSpeed = new double [_rows.length-2];
        if (_rightSpeedCol!=-1)
            _rightSpeed = new double [_rows.length-2];
        _distance = new double [_rows.length-2];
        
        for (int r=0; r<_rows.length-2;r++) {
            String[] values=(String[])_rows[r];
            if (_leftSpeedCol!=-1) {
                _leftSpeed[r]=Double.parseDouble(values[_leftSpeedCol]);
            }
            if (_rightSpeedCol!=-1) {
                _rightSpeed[r]=Double.parseDouble(values[_rightSpeedCol]);
                if (_rightSpeed[r]>0)
                    _hasRightSpeed=true;
            }
            _distance[r]=(speed(r)/3600)*0.1;
        }
    }
    
    public String name() {
        return _name;
    }
    
    public int length() {
        return _rows.length-2;
    }

    public boolean hasSpeed() {
        return _leftSpeedCol!=-1 || _rightSpeedCol!=-1;
    }
    
    public boolean hasLeftSpeed() {
        return _leftSpeedCol!=-1;
    }
    
    public boolean hasRightSpeed() {
        return _hasRightSpeed;
    }
    
    public boolean hasSteer() {
        return hasLeftSpeed() && hasRightSpeed();
    }
    
    public boolean hasLatAccel() {
        return hasSteer();
    }
    
    public boolean hasLongAccel() {
        return hasSpeed();
    }
    
    public boolean hasRPM() {
        return _rpmCol!=-1;
    }
    
    public boolean hasDistance() {
        return hasSpeed();
    }
    
    public boolean hasTPS() {
        return _tpsCol!=-1;
    }

    public boolean hasMAP() {
        return _mapCol!=-1;
    }
    
    public boolean hasLambda() {
        return _lambdaCol!=-1;
    }

    public boolean hasWater() {
        return _waterCol!=-1;
    }

    public boolean hasAirT() {
        return _airTCol!=-1;
    }

    public boolean hasOilT() {
        return _oilTCol!=-1;
    }
    
    public boolean hasOilP() {
        return _oilPCol!=-1;
    }

    public boolean hasSlip() {
        return _slipCol!=-1;
    }
    
    public boolean hasTurbo() {
        return _turboCol!=-1;
    }

    public boolean hasBoost() {
        return _boostCol!=-1;
    }
    
    public double speed(int index) {
        assert hasSpeed();
        assert index>=0;
        assert index<length();
        
        
        if (_leftSpeedCol!=-1 && _rightSpeedCol!=-1 &&
                _leftSpeed[index]>0 && _rightSpeed[index]>0)
            return (_leftSpeed[index]+_rightSpeed[index])/2;
        else if (_leftSpeedCol!=-1) 
            return _leftSpeed[index];
        else 
            return _rightSpeed[index];
    }
    
    public double leftSpeed(int index) {
        assert hasLeftSpeed();
        assert index>=0;
        assert index<length();
        
        return _leftSpeed[index];
    }
    
    public double rightSpeed(int index) {
        assert hasRightSpeed();
        assert index>=0;
        assert index<length();
        
        return _rightSpeed[index];
    }
    
    public double distance(int index) {
        assert hasDistance();
        assert index>=0;
        assert index<length();
        
        return _distance[index];
    }
    
    public double rpm(int index) {
        assert hasTPS();
        assert index>=0;
        assert index<length();
        
        return Double.parseDouble(((String[])_rows[index])[_rpmCol]);
    }
    
    public double tps(int index) {
        assert hasTPS();
        assert index>=0;
        assert index<length();
        
        return Double.parseDouble(((String[])_rows[index])[_tpsCol]);
    }
    
    public double map(int index) {
        assert hasMAP();
        assert index>=0;
        assert index<length();
        
        return Double.parseDouble(((String[])_rows[index])[_mapCol]);
    }
    
    public double lambda(int index) {
        assert hasLambda();
        assert index>=0;
        assert index<length();
        
        return Double.parseDouble(((String[])_rows[index])[_lambdaCol]);
    }
    
    public double water(int index) {
        assert hasWater();
        assert index>=0;
        assert index<length();
        
        return Double.parseDouble(((String[])_rows[index])[_waterCol]);
    }
    
    public double airT(int index) {
        assert hasAirT();
        assert index>=0;
        assert index<length();
        
        return Double.parseDouble(((String[])_rows[index])[_airTCol]);
    }
    
    public double oilT(int index) {
        assert hasOilT();
        assert index>=0;
        assert index<length();
        
        return Double.parseDouble(((String[])_rows[index])[_oilTCol]);
    }
    
    public double oilP(int index) {
        assert hasOilP();
        assert index>=0;
        assert index<length();
        
        return Double.parseDouble(((String[])_rows[index])[_oilPCol]);
    }
    
    public double slip(int index) {
        assert hasSlip();
        assert index>=0;
        assert index<length();
        
        return Double.parseDouble(((String[])_rows[index])[_slipCol]);
    }
    
    public double turbo(int index) {
        assert hasSlip();
        assert index>=0;
        assert index<length();
        
        return Double.parseDouble(((String[])_rows[index])[_turboCol]);
    }

    public double boost(int index) {
        assert hasSlip();
        assert index>=0;
        assert index<length();
        
        return Double.parseDouble(((String[])_rows[index])[_boostCol]);
    }

    public boolean isKPA() {
        return _KPA;
    }

    public boolean isKPH() {
        return _KPH;
    }

    public boolean isDegC() {
        return _degC;
    }

    public boolean isLambda() {
        return _lambda;
    }
    
}
