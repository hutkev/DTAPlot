/**
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
package uk.co.kevinjjones.model;

import java.util.ArrayList;

/**
 * Basic double sampler, counts values into buckets
 */
public class DoubleSampler extends Sampler<Double> {
    
    private Double _min;
    private Double _max;
    private int _buckets;
    private int _count=0;
    
    public DoubleSampler(Double min, Double max, int buckets) {
        assert(buckets>0);
        assert(min<=max);
        _min=min;
        _max=max;
        _buckets=buckets;
        _count=0;
    }
    
    @Override
    public Double min() {
        return _min;
    }
    
    @Override
    public Double max() {
        return _max;
    }
    
    @Override
    public int buckets() {
        return _buckets;
    }
    
    @Override
    public WithError<ArrayList<Integer>,SamplerRangeError> sample(Double[] values) {
        
        WithError<ArrayList<Integer>,SamplerRangeError> result=new WithError(new ArrayList<Integer>());
        
        ArrayList<Integer> d=new ArrayList();
        while (d.size()<_buckets)
            d.add(new Integer(0));

        double size=(_max.doubleValue()-_min.doubleValue())/_buckets;
        for (int i=0;i<values.length;i++) {
            double o=values[i];
            if (o<_min || o>_max) {
                result.addError(new SamplerRangeError(this,_count,o));
            } else {
                o-=_min;
                int at=(int)(o/size);
                d.set(at,new Integer(d.get(at).intValue()+1));
            }
            _count+=1;
        }
        
        result.setValue(new ArrayList<Integer>(d));
        return result;
    }
}

