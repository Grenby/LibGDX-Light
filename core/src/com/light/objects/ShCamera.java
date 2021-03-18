package com.light.objects;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;


public class ShCamera extends PerspectiveCamera implements ShaderUniform {

    private int positionUniform=-1;
    private int invProjectionViewUniform =-1;

    public ShCamera(){}

    public ShCamera(float fieldOfViewY, float viewportWidth, float viewportHeight) {
        super(fieldOfViewY, viewportWidth, viewportHeight);
    }

    @Override
    public void setLocation(ShaderProgram program) {
        positionUniform = program.getUniformLocation(getNameUniform());
        invProjectionViewUniform=program.getUniformLocation("u_invProjectionView");

    }

    @Override
    public void setUniform(ShaderProgram program) {
        program.setUniformf(positionUniform,position);
        program.setUniformMatrix4fv(invProjectionViewUniform,invProjectionView.val,0,16);
    }

    @Override
    public String getNameUniform() {
        return "u_cameraPosition";
    }

    @Override
    public String getTypeUniform() {
        return "vec3";
    }
}
