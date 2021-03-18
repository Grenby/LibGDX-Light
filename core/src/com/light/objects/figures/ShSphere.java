package com.light.objects.figures;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.light.objects.Object3d;
import com.light.objects.ShaderDistance;
import com.light.objects.ShaderStruct;

public class ShSphere extends Object3d implements ShaderStruct, ShaderDistance {

    private final static String struct ="struct sphere{\n" +
            "    vec3 p;\n" +
            "    float r;\n" +
            "};\n";
    private final static String SDF = "float sphereSDF(vec3 p,float r){\n" +
            "    return length(p)-r;\n" +
            "}\n";

    private static int numCreated = 0;

    private final int id;

    public float r=1;
    private int radiusUniform=-1;

    public ShSphere(){
        this.id=numCreated;
        setNameUniform("u_sphere"+id);
        numCreated++;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getSDF() {
        return SDF;
    }

    @Override
    public String getDistance() {
        String name = getNameUniform();
        return "sphereSDF(p-"+name+".p,"+name+".r)";
    }

    @Override
    public String getStruct() {
        return struct;
    }

    @Override
    public void setLocation(ShaderProgram program) {
        String name = getNameUniform();
        positionUniform = program.getUniformLocation(name+".p");
        radiusUniform = program.getUniformLocation(name+".r");
    }

    @Override
    public void setUniform(ShaderProgram program) {
        super.setUniform(program);
        program.setUniformf(radiusUniform,r);
    }

    @Override
    public String getTypeUniform() {
        return "sphere";
    }
}
