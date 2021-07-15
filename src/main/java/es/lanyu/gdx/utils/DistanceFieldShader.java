package es.lanyu.gdx.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;

public class DistanceFieldShader extends ShaderProgram {
//	private static class DistanceFieldShader extends ShaderProgram {                                                            
    public DistanceFieldShader () {                                                                                         
        // The vert and frag files are copied from http://git.io/yK63lQ (vert) and http://git.io/hAcw9Q (the frag)          
        super(Gdx.files.internal("data/shaders/distance-field.vert"), Gdx.files.internal("data/shaders/distance-field.frag"));
        if (!isCompiled()) {                                                                                                
            throw new RuntimeException("Shader compilation failed:\n" + getLog());                                          
        }                                                                                                                   
    }                                                                                                                       

    /** @param smoothing a value between 0 and 1 */                                                                         
    public void setSmoothing (float smoothing) {                                                                            
        float delta = 0.5f * MathUtils.clamp(smoothing, 0, 1);                                                              
        setUniformf("u_lower", 0.5f - delta);                                                                               
        setUniformf("u_upper", 0.5f + delta);                                                                               
    }                                                                                                                       
}   