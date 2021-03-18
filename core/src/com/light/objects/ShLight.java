package com.light.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShLight extends Object3d implements ShaderStruct {

    private static final String struct =
            "struct Light{\n" +
            "    vec3 pos;\n" +
            "    vec3 color;\n" +
            "};\n";

    private static int numCreated=0;

    public final Color color = new Color(Color.WHITE);
    private int colorUniform=-1;
    private int id=-1;

    public ShLight(){
        id=numCreated;
        numCreated++;
        setNameUniform("u_light"+id);
    }

    @Override
    public String getStruct() {
        return struct;
    }

    @Override
    public void setLocation(ShaderProgram program) {
        String name = getNameUniform();
        positionUniform = program.getUniformLocation(name+".pos");
        colorUniform = program.getUniformLocation(name+".color");
    }

    @Override
    public void setUniform(ShaderProgram program) {
        super.setUniform(program);
        program.setUniformf(colorUniform,color.r,color.g,color.b);
    }

    @Override
    public String getNameUniform() {
        return "u_light" + id;
    }

    @Override
    public String getTypeUniform() {
        return "Light";
    }
}
