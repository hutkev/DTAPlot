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
package uk.co.kevinjjones.vehicle;

/**
 */
public class Basedata  {
    
    private static Basedata _instance = null;
    
    // Singleton access
    public static Basedata getInstance() {
        if (_instance == null) {
            _instance = new Basedata();
        }
        return _instance;
    }
    
    private double _weight;
    
    private Basedata() {
        _weight=Double.NaN;
    }
    
    double weight() {
        return _weight;
    }
    
    public void setWeight(double weight) {
        _weight=weight;
    }
    
}
