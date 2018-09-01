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

vec3 rotate2(vec3 ray, vec2 angle) {
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
  ray = rotate2(ray, rotation*M_PI/180);

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

	vec2 leng;
	vec2 positionDistance;
	vec3 cr = cross(point, vec3(-point.z,0,point.x));
	t = (cr.x*point.x - cr.x*position.x + cr.y*point.y - cr.y*position.y + cr.z*point.z - cr.z*position.z)/(cr.x*cr.x + cr.y*cr.y + cr.z*cr.z);
	vec3 proj = position + t*cr;
	leng.x = sqrt((proj.x-point.x)*(proj.x-point.x) + (proj.y-point.y)*(proj.y-point.y) + (proj.z-point.z)*(proj.z-point.z));
	positionDistance.x = sqrt(proj.x*proj.x + proj.y*proj.y + proj.z*proj.z);

	t = (-point.z*point.x + point.z*position.x + point.x*point.z - point.x*position.z)/(point.z*point.z + point.x*point.x);
	proj = position + t*vec3(-point.z, 0, point.x);
	leng.y = sqrt((proj.x-point.x)*(proj.x-point.x) + (proj.y-point.y)*(proj.y-point.y) + (proj.z-point.z)*(proj.z-point.z));
	positionDistance.y = sqrt(proj.x*proj.x + proj.y*proj.y + proj.z*proj.z);

	vec2 finalAngle = acos((1 + leng*leng - positionDistance*positionDistance)/(2*leng));
	if (positionPolar.y < pointPolar.y) {
		finalAngle.y *= -1;
	}
	if (positionPolar.x < pointPolar.x == abs(positionPolar.x - pointPolar.x) < M_PI) {
		finalAngle.x *= -1;
	}
	vec2 spherePoint = vec2(pointPolar.x/M_PI/2+0.5,pointPolar.y/M_PI+0.5);
	color = texture2D(lightField, (floor(spherePoint*imageSize)+0.5)/imageSize - finalAngle/M_PI/imageSize);
}
