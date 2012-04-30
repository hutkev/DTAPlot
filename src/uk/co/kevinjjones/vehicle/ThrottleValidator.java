/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.kevinjjones.vehicle;

import uk.co.kevinjjones.model.ChunkValidator;
import uk.co.kevinjjones.model.Distribution;
import uk.co.kevinjjones.model.DoubleSampler;
import uk.co.kevinjjones.model.StreamError;

/**
 *
 * @author kjones
 */
public class ThrottleValidator extends ChunkValidator<Double> {
    
    @Override
    public StreamError[] validate(Double[] values) {
        
        // Sample into normal range
        DoubleSampler ds=new DoubleSampler(0.0,100.0,1);
        Distribution<Double> d=new Distribution(ds);
        d.sample(values);
        
        
        
        
        return null;
    }
    
}
