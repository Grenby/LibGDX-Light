attribute vec3 a_position;
uniform mat4 u_invProjectionView;

varying vec3 v_dir;

void main(){
    vec4 v1 = vec4(a_position,1);
    vec4 v2 = vec4(a_position,1);
    v1.z=-1;
    v2.z=1;

    v1 = u_invProjectionView*v1;
    v2 = u_invProjectionView*v2;


    v1/=v1.w;
    v2/=v2.w;

    v_dir = normalize(v2.xyz-v1.xyz);
    gl_Position = vec4(a_position,1);
}