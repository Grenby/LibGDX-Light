package com.light.objects.figures;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.light.objects.Object3d;
import com.light.objects.ShaderDistance;
import com.light.objects.ShaderStruct;

public class ShTorus extends Object3d implements ShaderStruct, ShaderDistance {

    private final static String struct =
            "struct torus{\n" +
            "    vec3 p;\n" +
            "    vec2 t;\n" +
            "};\n";
    private final static String SDF =
            "float torusSDF( vec3 p, vec2 t ){\n" +
            "  vec2 q = vec2(length(p.xz)-t.x,p.y);\n" +
            "  return length(q)-t.y;\n" +
            "}";

    private static int numCreated = 0;

    public final Vector2 r = new Vector2(4,1);
    private int radiusUniform=-1;

    public ShTorus(){
        setNameUniform("u_torus"+numCreated++);
    }

    @Override
    public String getSDF() {
        return SDF;
    }

    @Override
    public String getDistance() {
        String name = getNameUniform();
        return "torusSDF(p-"+name+".p,"+name+".t)";
    }

    @Override
    public String getStruct() {
        return struct;
    }

    @Override
    public void setLocation(ShaderProgram program) {
        String name = getNameUniform();
        positionUniform = program.getUniformLocation(name+".p");
        radiusUniform = program.getUniformLocation(name+".t");
    }

    @Override
    public void setUniform(ShaderProgram program) {
        super.setUniform(program);
        program.setUniformf(radiusUniform,r);
    }

    @Override
    public String getTypeUniform() {
        return "torus";
    }
}