package com.light.objects;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public interface ShaderUniform {

    /**
     * void set locations id for all necessary params for working with struct
     * @param program - shader where params are to set
     */
    void setLocation(ShaderProgram program);

    /**
     * set params by their location id in shader
     * @param program - shader where params are to set
     */
    void setUniform(ShaderProgram program);

    /**
     * @return unique name of variable for declaration
     */
    String getNameUniform();

    /**
     * @return name type for describe in shader
     */
    String getTypeUniform();
}
