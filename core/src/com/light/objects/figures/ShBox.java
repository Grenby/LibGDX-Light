package com.light.objects.figures;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.light.objects.Object3d;
import com.light.objects.ShaderDistance;
import com.light.objects.ShaderStruct;

public class ShBox extends Object3d implements ShaderDistance, ShaderStruct {

    private final static String STRUCT =
            "struct box{\n" +
            "   vec3 p;\n" +
            "   vec3 b;\n" +
            "};\n";
    private final static String SDF =
            "float boxSDF(vec3 p,vec3 b){\n"+
            "   vec3 q = abs(p) - b;\n"+
            "   return length(max(q,0.0)) + min(max(q.x,max(q.y,q.z)),0.0);\n"+
            "}\n";


    private static int numCreated=0;

    public final Vector3 b=new Vector3(1,1,1);
    private int bUniform=-1;


    public ShBox(){
        setNameUniform("u_box"+numCreated++);
    }

    @Override
    public String getSDF() {
        return SDF;
    }

    @Override
    public String getDistance() {
        String name = getNameUniform();
        return "boxSDF(p-"+name+".p,"+name+".b)";
    }

    @Override
    public String getStruct() {
        return STRUCT;
    }

    @Override
    public void setUniform(ShaderProgram program) {
        super.setUniform(program);
        program.setUniformf(bUniform,b);
    }

    @Override
    public void setLocation(ShaderProgram program) {
        String name = getNameUniform();
        positionUniform = program.getUniformLocation(name+".p");
        bUniform = program.getUniformLocation(name+".b");
    }

    @Override
    public String getTypeUniform() {
        return "box";
    }
}
