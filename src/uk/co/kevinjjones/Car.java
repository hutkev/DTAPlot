/*
 Copyright 2012 Kevin J. Jones (http://www.kevinjjones.co.uk)

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

/**
 * This is not currently in use, just being saved in case I want to
 * use again later.
 */
public class Car {
    
    public static final int Track=1500;
    public static final int WheelBase=2380;
    public static final int Weight=580;
    
    private static final int Steps=599;
    
    private static Car _instance = null;
    private double _radiusTable[];
    
    protected Car() {
        // Exists only to defeat instantiation.
    }

    // Singleton access
    public static Car getInstance() {
        if (_instance == null) {
            _instance = new Car();
        }
        return _instance;
    }
    
    public double getDegrees(double outerSpeed, double innerSpeed) {
        assert(outerSpeed>innerSpeed);
        
        if (_radiusTable==null)
            computeRadius();

        if (outerSpeed==innerSpeed ||
                outerSpeed==0 || innerSpeed==0)
            return 0;
        
        double ratio=outerSpeed/innerSpeed;
        if (ratio<_radiusTable[Steps-1]) {
            return 0; // We are going straight
        }
        
        for (int i=0; i<Steps; i++) {
            if (ratio<_radiusTable[i]) {
                double deg=i;
                deg/=10;
                deg-=1;
                return deg;
            }
        }
    
        return 0;
    }
    
    public double getLatAccel(double outerSpeed, double innerSpeed) {
        double deg=getDegrees(outerSpeed,innerSpeed);
        double outerRadius=WheelBase/Math.tan(Math.toRadians(deg));
        double speed=outerSpeed*1000/3600;
        double a=speed*speed/outerRadius;
        return a/9.8;
    }
    
    private void computeRadius() {
        _radiusTable=new double[Steps];
        for (int i=0; i<Steps; i++) {
            
            double outerAngle=(i+1)*0.1;
            double outerRadius=WheelBase/Math.tan(Math.toRadians(outerAngle));
            if (outerRadius>Track) {
                double innerRadius=outerRadius-Track;
                double innerAngle=Math.toDegrees(Math.atan((WheelBase/innerRadius)));
            
                double ratio=Math.sin(Math.toRadians(innerAngle))/
                                Math.sin(Math.toRadians(outerAngle));
                _radiusTable[i]=ratio;
            } else {
                _radiusTable[i]=0;
            }
                
        }
    }
    
    
}
