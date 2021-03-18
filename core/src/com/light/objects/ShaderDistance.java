package com.light.objects;

/**
 * interface for description and working with distance field
 */
public interface ShaderDistance {
    /**
     * @return code of signed distance function
     */
    String getSDF();

    /**
     * @return call SDF
     */
    String getDistance();
}
