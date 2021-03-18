package com.light.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShColor extends Color implements ShaderUniform {
    private int colorUniform = -1;

    @Override
    public void setLocation(ShaderProgram program) {
        String name = getNameUniform();
        colorUniform = program.getUniformLocation(name);
    }

    @Override
    public void setUniform(ShaderProgram program) {
        program.setUniformf(colorUniform,r,g,b);
    }

    @Override
    public String getNameUniform() {
        return "u_color";
    }

    @Override
    public String getTypeUniform() {
        return "vec3";
    }
}
