package com.light.objects;

import com.badlogic.gdx.math.Vector3;

public class CombinedColor {

    public final Vector3 ambient = new Vector3();
    public final Vector3 diffuse = new Vector3();
    public final Vector3 specular= new Vector3();

    public float kAmbient=0;
    public float kSpecular=0;
    public float kDiffuse=0;

    public String getStruct() {
        return "struct CombinedColor{" +
                "vec4 ambient;" +
                "vec4 diffuse;" +
                "vec4 specular;" +
                "float ka;" +
                "float ks;" +
                "float kd;" +
                "};";
    }
}
