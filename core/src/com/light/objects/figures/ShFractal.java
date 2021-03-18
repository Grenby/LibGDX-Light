package com.light.objects.figures;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.light.objects.Object3d;
import com.light.objects.ShaderDistance;
import com.light.objects.ShaderStruct;

public class ShFractal extends Object3d implements ShaderDistance, ShaderStruct {

    private final static String STRUCT =
            "struct fractal{\n" +
            "    vec3 p;\n" +
            "};\n";
    private final static String SDF =
            "float fractalSDF(vec3 z){\n" +
                    "vec3 a1 = vec3(1,1,1);\n" +
                    "vec3 a2 = vec3(-1,-1,1);\n" +
                    "vec3 a3 = vec3(1,-1,-1);\n" +
                    "vec3 a4 = vec3(-1,1,-1);\n" +
                    "vec3 c;\n" +
                    "int n = 0;\n" +
                    "float dist, d;\n" +
                    "while (n < 50) {\n" +
                    "   c = a1; dist = length(z-a1);\n" +
                    "   d = length(z-a2); if (d < dist) { c = a2; dist=d; }\n" +
                    "   d = length(z-a3); if (d < dist) { c = a3; dist=d; }\n" +
                    "   d = length(z-a4); if (d < dist) { c = a4; dist=d; }\n" +
                    "   z = 2*z-c*(2-1.0);\n" +
                    "   n++;\n" +
                    "}\n" +
                    "return length(z) * pow(2, float(-n));\n" +
                    "}\n";

    private static int numCreated=0;

    public final Vector3 nor=new Vector3(0,0,1);
    private int normalUniform=-1;


    public ShFractal(){
        setNameUniform("u_fractal"+numCreated++);
    }

    @Override
    public String getSDF() {
        return SDF;
    }

    @Override
    public String getDistance() {
        String name = getNameUniform();
        return "fractalSDF(p-"+name+".p)";
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
    }

    @Override
    public String getTypeUniform() {
        return "fractal";
    }
}
