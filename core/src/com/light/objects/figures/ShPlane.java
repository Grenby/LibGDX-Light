package com.light.objects.figures;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.light.objects.Object3d;
import com.light.objects.ShaderDistance;
import com.light.objects.ShaderStruct;

public class ShPlane extends Object3d implements ShaderDistance, ShaderStruct {

    private final static String STRUCT =
            "struct Plane{\n" +
            "    vec3 p;\n" +
            "    vec3 n;\n" +
            "};\n";
    private final static String SDF =
            "float planeSDF(vec3 p,vec3 n){\n" +
            "    return abs(dot(p,n));\n" +
            "}";


    private static int numCreated=0;

    public final Vector3 nor=new Vector3(0,0,1);
    private int normalUniform=-1;


    public ShPlane(){
        setNameUniform("u_plane"+numCreated++);
    }

    @Override
    public String getSDF() {
        return SDF;
    }

    @Override
    public String getDistance() {
        String name = getNameUniform();
        return "planeSDF(p-"+name+".p,"+name+".n)";
    }

    @Override
    public String getStruct() {
        return STRUCT;
    }

    @Override
    public void setUniform(ShaderProgram program) {
        super.setUniform(program);
        program.setUniformf(normalUniform,nor);
    }

    @Override
    public void setLocation(ShaderProgram program) {
        String name = getNameUniform();
        positionUniform = program.getUniformLocation(name+".p");
        normalUniform = program.getUniformLocation(name+".n");
    }

    @Override
    public String getTypeUniform() {
        return "Plane";
    }
}
