#version 130

#define M_PI 3.14159265

in vec2 texcoord;

uniform sampler2D lightField;
uniform int subImageSize;

uniform vec3 position;
uniform vec2 rotation;

out vec4 color;

vec3 rotate(vec3 ray, vec2 angle) {

  //rotate y
  float y = -sin(angle.y)*ray.z;
  float z = cos(angle.y)*ray.z;
  ray.y = y;
  ray.z = z;

  //rotate x
  float x = -sin(angle.x)*ray.z;
  z = cos(angle.x)*ray.z;
  ray.x = x;
  ray.z = z;

  return ray;
}

void main(void) {
	//vec3 ray = rotate(vec3(0,0,-1), vec2(texcoord.x*M_PI, texcoord.y*M_PI/2));
	vec3 ray = vec3(texcoord.x, texcoord.y, -1);
  ray = rotate (ray, rotation);

  float a = ray.x*ray.x + ray.y*ray.y + ray.z*ray.z;
  float b = 2*ray.x*position.x + 2*ray.y*position.y + 2*ray.z*position.z;
  float c = position.x*position.x + position.y*position.y + position.z*position.z - 1;
  float delta = b*b - 4*a*c;
  if (delta <= 0) {
    discard;
  }
  float t = (-b + sqrt(delta))/(2*a);
  if (t <= 0) {
    discard;
  }
  vec3 point = ray*t + position;
  color = vec4(1,0,1,1);
}
