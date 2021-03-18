#ifdef GL_ES
precision mediump float;
#endif
#define MAX_DIST 100.0
#define MIN_DIST 0.001
#define MAX_STEP 100

struct interstion{
    vec3 c;
    vec3 p;
    float l;
};

struct light{
    vec3 p;
    vec3 c;
};
struct sphere{
    vec3 p;
    vec3 c;
    float r;
};

uniform vec3 u_color;
uniform light u_light0;
uniform vec3 u_cameraPosition;
uniform sphere u_sphere0;
uniform sphere u_sphere1;
uniform sphere u_sphere2;
uniform sphere u_sphere3;
varying vec3 v_dir;

float SDF_sphere(vec3 p,float r){
    return length(p)-r;
}
interstion sceneSFD(vec3 p){
    interstion intersect;
    float d;
    intersect.l = MAX_DIST;
    d = SDF_sphere(p-u_sphere0.p,u_sphere0.r);
    if (d<intersect.l){
        intersect.l=d;
        intersect.c=u_sphere0.c;
    }
    d = SDF_sphere(p-u_sphere1.p,u_sphere1.r);
    if (d<intersect.l){
        intersect.l=d;
        intersect.c=u_sphere1.c;
    }
    d = SDF_sphere(p-u_sphere2.p,u_sphere2.r);
    if (d<intersect.l){
        intersect.l=d;
        intersect.c=u_sphere2.c;
    }
    d = SDF_sphere(p-u_sphere3.p,u_sphere3.r);
    if (d<intersect.l){
        intersect.l=d;
        intersect.c=u_sphere3.c;
    }
    return intersect;
}
float shortestDistanceToSurface(vec3 dir){
    vec3 start = u_cameraPosition;
    float lenWay = 0;
    for (int i=0;i<MAX_STEP;i++){
        //float dist = sceneSFD(start);
        //if (dist<MIN_DIST)return lenWay;
        //lenWay+=dist;
        //if (lenWay>MAX_DIST)return MAX_DIST;
        //start+=dist*dir;
    }
    return MAX_DIST;
}
vec3 getNormal(vec3 pos){
    float epsilon=MIN_DIST;
    //float distX=sceneSFD(pos+vec3(epsilon,0,0))-sceneSFD(pos-vec3(epsilon,0,0));
    //float distY=sceneSFD(pos+vec3(0,epsilon,0))-sceneSFD(pos-vec3(0,epsilon,0));
    //float distZ=sceneSFD(pos+vec3(0,0,epsilon))-sceneSFD(pos-vec3(0,0,epsilon));
    return normalize(vec3(distX,distY,distZ));
}
vec3 getColor(vec3 p){
    float ambientStrength = 0.4;
    float specularStrength  = 2;
    float reflectionStrength = 2;
    vec3 N = getNormal(p);
    vec3 L = normalize(u_light0.p-p);
    vec3 V = normalize(u_cameraPosition-p);
    vec3 R = normalize(reflect(-L,N));
    vec3 T = normalize(2*dot(N,V)*N-V);
    float diff=max(dot(N,L),0.0);
    vec3 ambient = ambientStrength*u_light0.c;
    vec3 objColor = u_color;
    vec3 diffuse = diff*u_light0.c;
    vec3 specular = pow(max(dot(V,R),0.0),32)*specularStrength * u_light0.c;
    vec3 reflectColor = (diff)*reflectionStrength*u_color;
    vec3 res = (ambient+diffuse+specular+reflectColor) * (objColor);
    return res;
}
void main(){
    vec3 color=vec3(0,0,0);
    float len=shortestDistanceToSurface(v_dir);
    if (len>=MAX_DIST)color=vec3(0.47,0.47,0.47);
    else color=getColor(u_cameraPosition+len*v_dir);
    gl_FragColor = vec4(color,1);
}