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
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.fitting.CurveFitter;
import org.apache.commons.math3.optim.nonlinear.vector.jacobian.LevenbergMarquardtOptimizer;

/**
 * Wheel virtual stream
 */
public class SmoothStream implements ROStream {
    
    private ROStream _base;
    private double[] _curve=null;

    public SmoothStream(ROStream base) {
        _base = base;
    }
    
    @Override
    public void setMeta(String id, String value) {
        _base.setMeta(id, value);
    }

    @Override
    public String getMeta(String id) {
        return _base.getMeta(id);
    }

    @Override
    public String name() {
        return _base.name();
    }

    @Override
    public String description() {
        return _base.description();
    }

    @Override
    public String axis() {
        return _base.axis();
    }

    @Override
    public String units() {
        return _base.units();
    }

    @Override
    public int size() {
        return _base.hashCode();
    }

    @Override
    public String getString(int position) {
        return _base.getString(position);
    }

    @Override
    public long getTick(int position) throws ParseException {
        return _base.getTick(position);
    }

    @Override
    public double getNumeric(int position) throws NumberFormatException {
        if (_curve == null) {
            curveFit();
        }

        return _curve[position];
    }
    
    @Override
    public String[] getStringSet() {
        return _base.getStringSet();
    }

    @Override
    public Double[] toArray() throws NumberFormatException {
        return _base.toArray();
    }
    
    private void curveFit() {
        if (_curve == null) {
            Double[] data=toArray();
            int points = data.length;
            int blockSize = 8;
            _curve=new double[points];
            
            int idx = 0;
            while (idx<points) {
                
                // Skip zeros, they curve fit badly 
                if (data[idx]==0) {
                    _curve[idx]=0;
                    idx++;
                } else {
                    // Consume a block
                    int bcount=0;
                    while (idx+bcount<points && bcount<blockSize) {
                        if (data[idx+bcount]==0) {
                            break;
                        }
                        bcount++;
                    }
                    
                    // Skip short blocks
                    if (bcount<5) {
                        for (int i=0; i<bcount; i++) {
                            _curve[idx+i]=data[idx+i];
                        }
                        idx+=bcount;
                    } else {
                        // Try to fit this
                        CurveFitter fitter = new CurveFitter(new LevenbergMarquardtOptimizer());
                        double[] init=new double[4];
                        for (int i=0; i<bcount; i++) {
                            fitter.addObservedPoint(i,data[idx+i]);
                        }
                        final double[] best = fitter.fit(new PolynomialFunction.Parametric(), init);
                        final PolynomialFunction fitted = new PolynomialFunction(best);

                        for (int i=0; i<bcount; i++) {
                            _curve[idx+i] = fitted.value(i);
                            
                            // Negative fits upset the graphing
                            if (_curve[idx+i]<0) {
                                _curve[idx+i] = 0;
                            }
                        }
                        idx+= bcount;
                    }
                }
            }
        }
    }
}
