package com.light.objects;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

public abstract class Object3d implements ShaderUniform{

    public final Vector3 position = new Vector3();
    protected int positionUniform = -1;
    private String uniformName;

    public void setNameUniform(String uniformName) {
        this.uniformName = uniformName;
    }

    @Override
    public String getNameUniform() {
        return uniformName;
    }

    @Override
    public void setUniform(ShaderProgram program) {
        program.setUniformf(positionUniform,position);

    }
}
