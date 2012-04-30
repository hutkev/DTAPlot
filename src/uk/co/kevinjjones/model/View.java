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
package uk.co.kevinjjones.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * A viewport over some stream of data in multiple columns (streams)
 */
public class View {
    
    private static final int minGrowth=128;
    private static final int maxGrowth=1024;
    private static final SimpleDateFormat fmt1=new SimpleDateFormat("mm:ss.SSS");
    private static final SimpleDateFormat fmt2=new SimpleDateFormat("HH:mm:ss.SSS");
    
    private ArrayList<ROStream> _streams=new ArrayList();
    private HashMap<String,Integer> _streamNames=new HashMap();
    
    private String[][] _rawData=new String[0][0];
    private ArrayList<Integer> _rawDataUsed=new ArrayList();
    
    private double[][] _numericData=new double[0][0];
    private ArrayList<Integer> _numericDataUsed=new ArrayList();
    
    private long[][] _tickData=new long[0][0];
    private ArrayList<Integer> _tickDataUsed=new ArrayList();
    
    public View() {
    }
    
    public boolean hasStream(String name) {
        return (_streamNames.get(name)!=null);
    }
    
    public RWStream createStream(String name,String desc,String axis,String units) {
        Integer idx=_streamNames.get(name);
        if (idx==null) {
            idx=_streams.size();
            RWStream s=new RWStream(name,desc,axis,units,this,idx);
            _streams.add(s);
            _streamNames.put(name, idx);
            scaleDataArrays(idx);
            return s;
        } else {
            return null;
        }
    }
    
    public ROStream getStream(String name) {
        Integer idx=_streamNames.get(name);
        if (idx!=null) {
            return _streams.get(idx);
        } else {
            return null;
        }
    }
    
    public ROStream getStream(int idx) {
        return _streams.get(idx);
    }
    
    public int addVirtualStream(String name, ROStream s) {
        Integer idx=_streamNames.get(name);
        assert(idx==null);

        idx=_streams.size();
        _streams.add(s);
        _streamNames.put(name, idx);
        return idx;
    }
    
    public int streamCount() {
        return _streams.size();
    }
    
    protected void addData(int idx, String d) {
        assert(idx<_streams.size());
        
        // Add to raw space for now
        int p=_rawDataUsed.get(idx);

        // Do we need more space
        if (_rawData[idx]==null) 
            _rawData[idx]=new String[0];
        if (p==_rawData[idx].length) {
            int size=_rawData[idx].length+getGrowthFactor(_rawData[idx].length);
            String[] rawData=new String[size];
            System.arraycopy(_rawData[idx], 0, rawData, 0, _rawData[idx].length);
            _rawData[idx]=rawData;
        }

        _rawData[idx][p]=d;
        _rawDataUsed.set(idx, p+1);
    }
    
    protected int getDataLength(int idx) {
        assert(idx<_streams.size());
        return _rawDataUsed.get(idx);
    }
    
    protected String getString(int idx, int position) {
        assert(idx<_streams.size());
        assert(position<getDataLength(idx));
        return _rawData[idx][position];
    }
    
    protected double getNumeric(int idx, int position) throws NumberFormatException {
        assert(idx<_streams.size());
        assert(position<getDataLength(idx));
        
        // Force conversion to double if needed
        if (_numericData[idx]==null) {
            _numericData[idx]=new double[_rawDataUsed.get(idx)];
            for (int i=0; i<_numericData[idx].length; i++) {
                _numericData[idx][i]=Double.parseDouble(_rawData[idx][i]);
            }
            _numericDataUsed.set(idx,_rawDataUsed.get(idx));
        }
        
        return _numericData[idx][position];
    }
    
    protected long getTick(int idx, int position) throws ParseException {
        assert(idx<_streams.size());
        assert(position<getDataLength(idx));
        
        // Force conversion to ticks if needed
        if (_tickData[idx]==null) {
            _tickData[idx]=new long[_rawDataUsed.get(idx)];
            for (int i=0; i<_tickData[idx].length; i++) {
                _tickData[idx][i]=parseTick(_rawData[idx][i]);
            }
            _tickDataUsed.set(idx,_rawDataUsed.get(idx));
        }
        
        return _tickData[idx][position];
    }
    
    protected void reverse(int idx) {
        reverseString(_rawData[idx],_rawDataUsed.get(idx));
    }
    
    private long parseTick(String in) throws ParseException {
        try {
            Date d=fmt1.parse(in);
            return d.getTime();
        } catch (ParseException ex) {
            try {
                Date d = fmt2.parse(in);
                return d.getTime();
            } catch (ParseException ex1) {
                throw ex1;
            }
        }
    }
    
    private int getGrowthFactor(int size) {
        int g=2*size;
        if (g<minGrowth) g=minGrowth;
        if (g>maxGrowth) g=maxGrowth;
        return g;
    }
    
    private void scaleDataArrays(int idx) {
        
        while (_rawData.length<=idx) {
            String[][] rawData=new String [Math.max(4,_rawData.length*2)] [];
            System.arraycopy(_rawData, 0, rawData, 0, _rawData.length);
            _rawData=rawData;
        }
        while (_rawDataUsed.size()<_rawData.length)
            _rawDataUsed.add(0);
        
        while ( _numericData.length<=idx) {
            double[][] doubleData=new double [Math.max(4,_numericData.length*2)] [];
            System.arraycopy(_rawData, 0, doubleData, 0, _rawData.length);
            _numericData=doubleData;
        }
        while (_numericDataUsed.size()<_numericData.length)
            _numericDataUsed.add(0);
        
    }
    
    private static void reverseString(String[] a, int size) {
        int left = 0;
        int right = size - 1;

        while( left < right ) {
            // swap the values at the left and right indices
            String temp = a[left];
            a[left] = a[right];
            a[right] = temp;

            // move the left and right index pointers in toward the center
            left++;
            right--;
        }
    }
    
    private static void reverseInt(int[] a, int size) {
        int left = 0;
        int right = size - 1;

        while( left < right ) {
            // swap the values at the left and right indices
            int temp = a[left];
            a[left] = a[right];
            a[right] = temp;

            // move the left and right index pointers in toward the center
            left++;
            right--;
        }
    }
}
