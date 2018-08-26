#version 130

#define M_PI 3.14159265

in vec2 texcoord;

uniform sampler2D lightField;
uniform int subImageSize;
uniform ivec2 imageSize;

uniform vec3 position;
uniform vec3 positionPolar;
uniform vec2 rotation;

out vec4 color;

vec3 rotate(vec3 ray, vec2 angle) {
	//rotate x
	float y = cos(angle.x)*ray.y - sin(angle.x)*ray.z;
	float z = cos(angle.x)*ray.z + sin(angle.x)*ray.y;
	ray.y = y;
	ray.z = z;

	//rotate y
	float x = cos(angle.y)*ray.x - sin(angle.y)*ray.z;
	z = cos(angle.y)*ray.z + sin(angle.y)*ray.x;
	ray.x = x;
	ray.z = z;

	return ray;
}

void main(void) {
	//vec3 ray = rotate(vec3(0,0,-1), vec2(texcoord.x*M_PI, texcoord.y*M_PI/2));
	vec3 ray = vec3(texcoord.x, texcoord.y, -1);
  ray = rotate (ray, rotation*M_PI/180);

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
  vec2 pointPolar = vec2(atan(point.x,-point.z), asin(point.y));
  vec2 intermediateAngle = positionPolar.xy - pointPolar;
  vec3 intermediatePoint = vec3(sin(intermediateAngle.x)*positionPolar.z,
    sin(intermediateAngle.y)*positionPolar.z, -cos(intermediateAngle.x)*positionPolar.z + 1);
  float len = sqrt(intermediatePoint.x*intermediatePoint.x + intermediatePoint.y*intermediatePoint.y + intermediatePoint.z*intermediatePoint.z);
  vec2 finalAngle = vec2(atan(intermediatePoint.x,-intermediatePoint.z), asin(intermediatePoint.y/len));
  //color = texture2D(lightField, round((texcoord/2+0.5)*(imageSize+1))/(imageSize+1));
  vec2 spherePoint = vec2(pointPolar.x/M_PI/2+0.5,pointPolar.y/M_PI+0.5);
  color = texture2D(lightField, floor(spherePoint*(imageSize+1))/(imageSize+1) - finalAngle/M_PI/imageSize); //FIXME
}
