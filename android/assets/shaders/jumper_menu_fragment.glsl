#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

uniform float alpha;

void main() {
    vec4 color = v_color * texture2D(u_texture, v_texCoords);
    color.a *= alpha;

    gl_FragColor=color;
}