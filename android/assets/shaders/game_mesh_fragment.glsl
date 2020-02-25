#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;

uniform vec4 ambient_color;
uniform int fog_enabled;
uniform vec4 fog_center_color;
uniform vec4 fog_outside_color;
uniform float fog_center_strength;
uniform float fog_outside_strength;
uniform vec2 screen_size;

uniform int light_enabled;
uniform float light_strength;
uniform vec2 light_position;


void main() {
    float perc = 1.0 - (gl_FragCoord.x / screen_size.x);
    float orginalAlpha = v_color.a;
    vec4 color = v_color;

    if (light_enabled == 1) {
        float dst = distance(gl_FragCoord.xy, light_position);
        float light_param = clamp(1.0 - (dst / light_strength), 0.0, 1.0);
        vec4 light_color = vec4(1.0,1.0,1.0,1.0);
        color = color * mix(ambient_color,  light_color ,light_param);
    } else {
        color = color * ambient_color;
    }

    vec4 color2;
    if (fog_enabled == 1) {
        float offset_from_center = abs(((gl_FragCoord.x / screen_size.x)-0.5)*2.0);
        vec4 finalColor = mix(
        color,
        mix(fog_center_color, fog_outside_color, offset_from_center),
        mix(fog_center_strength, fog_outside_strength, offset_from_center));
        color2 = vec4(finalColor.rgb, orginalAlpha);
    } else {
        color2 = color;
    }

    gl_FragColor=color2;

}