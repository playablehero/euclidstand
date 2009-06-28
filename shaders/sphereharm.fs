varying vec3 DiffuseColor;

void main()
{
    gl_FragColor = vec4(DiffuseColor, 1.0);
}