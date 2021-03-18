package com.light;

import com.light.objects.ShaderDistance;
import com.light.objects.ShaderStruct;
import com.light.objects.ShaderUniform;
import java.util.HashSet;

public class ShGenerator {

    private final StringBuilder builder = new StringBuilder();

    private final HashSet<Class<? extends ShaderUniform>> types = new HashSet<>();

    private final StringBuilder structBuilder = new StringBuilder();
    private final StringBuilder distanceBuilder = new StringBuilder();
    private final StringBuilder uniformBuilder = new StringBuilder();
    private final StringBuilder sceneSFDBuilder = new StringBuilder();


    private float maxDist=100;
    private float minDist=0.001f;
    private int maxStep=1000;

    public float getMaxDist() {
        return maxDist;
    }

    public void setMaxDist(float maxDist) {
        this.maxDist = maxDist;
    }

    public float getMinDist() {
        return minDist;
    }

    public void setMinDist(float minDist) {
        this.minDist = minDist;
    }

    public int getMaxStep() {
        return maxStep;
    }

    public void setMaxStep(int maxStep) {
        this.maxStep = maxStep;
    }

    public <T extends ShaderUniform> T addUniform(T uniform){
        if (!types.contains(uniform.getClass())) {
            if (uniform instanceof ShaderStruct) structBuilder.append(((ShaderStruct) uniform).getStruct());
            if (uniform instanceof ShaderDistance) distanceBuilder.append(((ShaderDistance) uniform).getSDF());
            types.add(uniform.getClass());
        }

        if (uniform instanceof ShaderDistance){
            sceneSFDBuilder.append("res = min(res,");
            sceneSFDBuilder.append(((ShaderDistance) uniform).getDistance());
            sceneSFDBuilder.append(");\n");
        }
        uniformBuilder.append("uniform ");
        uniformBuilder.append(uniform.getTypeUniform());
        uniformBuilder.append(" ");
        uniformBuilder.append(uniform.getNameUniform());
        uniformBuilder.append(";\n");

        return uniform;
    }

    public void clear(){
        builder.setLength(0);
        structBuilder.setLength(0);
        distanceBuilder.setLength(0);
        uniformBuilder.setLength(0);

        types.clear();
    }

    public String build(){
        builder.append(
                "#ifdef GL_ES\n" +
                "precision mediump float;\n" +
                "#endif\n");
        builder.append("#define MAX_DIST ");
        builder.append(maxDist);
        builder.append("\n#define MIN_DIST ");
        builder.append(minDist);
        builder.append("\n#define MAX_STEP ");
        builder.append(maxStep);
        builder.append("\n");
        builder.append(structBuilder.toString());
        builder.append(uniformBuilder.toString());
        builder.append("varying vec3 v_dir;\n");
        builder.append(distanceBuilder.toString());

        builder.append("float sceneSFD(vec3 p){\nfloat res=MAX_DIST;");
        builder.append(sceneSFDBuilder.toString());
        builder.append("    return res;\n}");

        builder.append("\n" +
                "float shortestDistanceToSurface(vec3 dir){\n" +
                "    vec3 start = u_cameraPosition;\n" +
                "    float lenWay = 0;\n" +
                "    for (int i=0;i<MAX_STEP;i++){\n" +
                "        float dist = sceneSFD(start);\n" +
                "        if (dist<MIN_DIST)return lenWay;\n" +
                "        lenWay+=dist;\n" +
                "        if (lenWay>MAX_DIST)return MAX_DIST;\n" +
                "        start+=dist*dir;\n" +
                "    }\n" +
                "    return MAX_DIST;\n}\n" +
                "vec3 getNormal(vec3 pos){\n" +
                "    float epsilon=MIN_DIST;\n" +
                "    float distX=sceneSFD(pos+vec3(epsilon,0,0))-sceneSFD(pos-vec3(epsilon,0,0));\n" +
                "    float distY=sceneSFD(pos+vec3(0,epsilon,0))-sceneSFD(pos-vec3(0,epsilon,0));\n" +
                "    float distZ=sceneSFD(pos+vec3(0,0,epsilon))-sceneSFD(pos-vec3(0,0,epsilon));\n" +
                "    return normalize(vec3(distX,distY,distZ));\n}\n" +
                "vec3 getColor(vec3 p){\n" +
                "    float ambientStrength = 0.4;\n" +
                "    float specularStrength  =0.1;\n" +
                "    float reflectionStrength = 1;\n" +
                "    vec3 N = getNormal(p);\n" +
                "    vec3 L = normalize(u_light0.pos-p);\n" +
                "    vec3 V = normalize(u_cameraPosition-p);\n" +
                "    vec3 R = normalize(reflect(-L,N));\n" +
                "    vec3 T = normalize(2*dot(N,V)*N-V);\n" +
                "    float diff=max(dot(N,L),0.0);\n" +
                "    vec3 ambient = ambientStrength*u_light0.color;\n" +
                "    vec3 objColor = u_color;\n" +
                "    vec3 diffuse = diff*u_light0.color;\n" +
                "    vec3 specular = pow(max(dot(V,R),0.0),32)*specularStrength * u_light0.color;\n" +
                "    vec3 reflectColor = (diff)*reflectionStrength*u_color;\n" +
                "    vec3 res = (ambient+diffuse+specular+reflectColor) * (objColor);\n" +
                "    return res;\n}\n" +
                "void main(){\n" +
                "    vec3 color=vec3(0,0,0);\n" +
                "    float len=shortestDistanceToSurface(v_dir);\n" +
                "    if (len>=MAX_DIST)color=vec3(0.47,0.47,0.47);\n" +
                "    else color=getColor(u_cameraPosition+len*v_dir);\n" +
                "    gl_FragColor = vec4(color,1);\n" +
                "}");

        String result = builder.toString();
        clear();

        return result;
    }

    public static String getVertex(){
        return "attribute vec3 a_position;\n" +
                "uniform mat4 u_invProjectionView;\n" +
                "varying vec3 v_dir;\n" +
                "void main(){\n" +
                "    vec4 v1 = vec4(a_position,1);\n" +
                "    vec4 v2 = vec4(a_position,1);\n" +
                "    v1.z=-1;\n" +
                "    v2.z=1;\n" +
                "    v1 = u_invProjectionView*v1;\n" +
                "    v2 = u_invProjectionView*v2;\n" +
                "    v1/=v1.w;\n" +
                "    v2/=v2.w;\n" +
                "    v_dir = normalize(v2.xyz-v1.xyz);\n" +
                "    gl_Position = vec4(a_position,1);\n" +
                "}";
    }

}
