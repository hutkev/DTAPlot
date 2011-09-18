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

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class RunManager {

    public class Run {

        private int _id;
        private Log _log;
        private int _start;
        private int _end;
        private double[] _distance;
        private double[] _timeSlip;

        public Run(int id, Log log, int start, int end) {
            _id = id;
            _log = log;
            _start = start;
            _end = end;
        }

        public Log log() {
            return _log;
        }

        public String name() {
            return "Run " + _id + " " + _log.name();
        }

        public int length() {
            return _end - _start;
        }
        
        public void recalc(double[] maxDistance) {
            _timeSlip=new double[length()];
            for (int i=0; i<length() && i<maxDistance.length; i++) {
                assert(distance(i)<=maxDistance[i]);
                
                // Count forward time slots until we reach same distance
                int t=i;
                while (t<length() && distance(t)<=maxDistance[i]) 
                    t++;
                _timeSlip[i]=t-i;
            }
        }

        public double speed(int i) {
            return _log.speed(_start + i);
        }

        public double distance(int i) {
            if (_distance==null) {
                _distance=new double[length()];
                double sum=0;
                for (int k=0; k<length(); k++) {
                    sum+=_log.distance(_start + k);
                    _distance[k]=sum;
                }
            }
            return _distance[i];
        }
        
        public double timeSlip(int i) {
            return _timeSlip[i]/10;
        }
        
        
        public double rpm(int i) {
            return _log.rpm(_start + i);
        }

        public double tps(int i) {
            return _log.tps(_start + i);
        }

        public double map(int i) {
            return _log.map(_start + i);
        }

        public double turbo(int i) {
            return _log.turbo(_start + i);
        }

        public double boost(int i) {
            return _log.boost(_start + i);
        }

        public double lambda(int i) {
            return _log.lambda(_start + i);
        }

        public double water(int i) {
            return _log.water(_start + i);
        }

        public double airT(int i) {
            return _log.airT(_start + i);
        }

        public double oilT(int i) {
            return _log.oilT(_start + i);
        }

        public double oilP(int i) {
            return _log.oilP(_start + i);
        }

        public double wheelSlip(int i) {
            return _log.slip(_start + i);
        }

        public boolean isKPA() {
            return _log.isKPA();
        }

        public boolean isKPH() {
            return _log.isKPH();
        }

        public boolean isDegC() {
            return _log.isDegC();
        }

        public boolean isLambda() {
            return _log.isLambda();
        }
    }
    private static RunManager _instance = null;
    private List<Log> _logs = new LinkedList<Log>();
    private List<Run> _runs = new LinkedList<Run>();
    private boolean _degC = true;
    private boolean _KPA = true;
    private boolean _KPH = true;
    private boolean _lambda = true;

    protected RunManager() {
        // Exists only to defeat instantiation.
    }

    // Simgleton access
    public static RunManager getInstance() {
        if (_instance == null) {
            _instance = new RunManager();
        }
        return _instance;
    }

    public void addLogfile(File file) throws RTException {
        
        // Parse logile and load runs from it
        Log l = new Log(file,_degC,_KPA,_KPH,_lambda);
        _logs.add(l);
        addRuns(l);
        
        // Calc max distance travelled at each time slot
        double[] maxDistance=new double[0];
        for(Run r : _runs) {
            if (maxDistance.length<r.length()) {
                double[] d=new double[r.length()];
                System.arraycopy(maxDistance, 0, d, 0, maxDistance.length);
                maxDistance=d;
            }
            
            for (int i=0; i<r.length(); i++) {
                if (r.distance(i)>maxDistance[i])
                    maxDistance[i]=r.distance(i);
            }
        }

        // Recalc runs for time slip using max Distance
        for(Run r : _runs) {
            r.recalc(maxDistance);
        }
    }

    public Log[] getLogs() {
        return _logs.toArray(new Log[0]);
    }

    public Run[] getRuns() {
        return _runs.toArray(new Run[0]);
    }

    private void addRuns(Log l) {

        // Look for runs
        int id = 1;
        int at = 1;
        while (true) {
            int start = findStartPoint(at, l);
            if (start == -1) {
                break;
            }
            int end = findEndPoint(start, l);
            if (end == -1) {
                break;
            }

            Run r = new Run(id++, l, start, end);
            _runs.add(r);
            at = end;
        }
    }

    /**
     * Test if the current row is a crossing point to the next speed unit. We
     * use two datums here, crossing from 0->1 kph and crossing over a 
     * speedBucket
     * @param data
     * @param start
     * @param index
     * @return 
     */
    private static int findStartPoint(int start, Log l) {

        // Loop speed until >100kph
        int r = start;
        for (; r < l.length(); r++) {
            if (l.speed(r) > 100) {
                break;
            }
        }
        // Didn't find 
        if (r == l.length()) {
            return -1;
        }

        // Now loop back to locate the start at lowest speed <10kph
        while (r > 0) {
            double speed = l.speed(r);
            double low = speed;
            if (speed < 10) {
                // In right area, find first lowest
                int t = r - 1;
                while (t > 0) {
                    double s = l.speed(t);
                    if (s < low) {
                        low = s;
                        r = t;
                    } else {
                        return r;
                    }
                    t--;
                }
            }
            r--;
        }

        // Not found;
        return -1;
    }

    private static int findEndPoint(int start, Log l) {

        // Loop speed until >100kph
        int r = start;
        for (; r < l.length(); r++) {
            if (l.speed(r) > 100) {
                break;
            }
        }
        // Didn't find 
        if (r == l.length()) {
            return -1;
        }

        // Continue loop until < 10km/h
        for (; r < l.length(); r++) {
            if (l.speed(r) < 10) {
                return r;
            }
        }
        return -1;
    }

    public boolean isDegC() {
        return _degC;
    }

    public void setDegC(boolean isDegC) {
        _degC = isDegC;
    }

    public boolean isKPA() {
        return _KPA;
    }

    public void setKPA(boolean isKPA) {
        _KPA = isKPA;
    }

    public boolean isKPH() {
        return _KPH;
    }

    public void setKPH(boolean isKPH) {
        _KPH = isKPH;
    }

    public boolean isLambda() {
        return _lambda;
    }

    public void setLambda(boolean isLambda) {
        _lambda = isLambda;
    }
}
