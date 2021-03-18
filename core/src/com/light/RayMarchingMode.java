package com.light;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.light.objects.*;
import com.light.objects.figures.*;

public class RayMarchingMode implements Screen {

    public static final int WIDTH = Gdx.graphics.getWidth();
    public static final int HEIGHT = Gdx.graphics.getHeight();

    private final ShCamera camera = new ShCamera(67f,WIDTH,HEIGHT);
    private final CameraInputController controller = new CameraInputController(camera);

    private final ShapeRenderer renderer = new ShapeRenderer();

    private final Mesh mesh = new Mesh(true,WIDTH*HEIGHT,0, VertexAttribute.Position());

    private final Array<ShaderUniform> uniforms = new Array<>();

    private ShaderProgram shader;

    private void cameraLogg(){
        System.out.println("_______________Camera Logg_______________");
        System.out.println("Width screen: " + WIDTH);
        System.out.println("Height screen: "+ HEIGHT);

        System.out.println("\nCamera position: "+ camera.position);

        System.out.println("\nCamera Direction: " + camera.direction+ " axis oz in camera's basis");
        System.out.println("Camera Up: " + camera.up+ " axis oy in camera's basis");
        System.out.println("Camera U*D: " + camera.up.cpy().crs(camera.direction)+ " axis ox in camera's basis");

        System.out.println("\nCamera view: \n"+ camera.view+"This is transform matrix from global basis to local camera's basis");
        System.out.println("\nCamera projection: \n"+ camera.projection +"This is projection ot screen matrix from local basis");
        System.out.println("\nCamera combined matrix:\n"+camera.combined+"This is camera.view*camera.projection");
        System.out.println("\nCamera invProjectionMatrix: \n"+camera.invProjectionView+ "THis is camera.combined^-1");

//        System.out.println("\n_______________example_______________");
//        Vector3 tmp = new Vector3(-10,0,10);
//        System.out.println("Point in global basis: "+ tmp);
//        System.out.println("Point in local basis: "+ tmp.cpy().mul(camera.view.cpy().inv()));
//        System.out.println("Point after projection: "+ tmp.cpy().mul(camera.view).mul(camera.projection));
//        System.out.println("Point after camera.combined: " + tmp.cpy().mul(camera.combined));
//        System.out.println();
//        System.out.println(new Vector3(1,1,-1).mul(camera.projection.cpy()).mul(camera.view));

    }

    private void setMesh(){
        int id=0;
        final float [] vertices = new float[WIDTH*HEIGHT*3];
        for (int i=1;i<=WIDTH;i++){
            for (int j=1;j<=HEIGHT;j++){
                vertices[id++] = 2.f*i/WIDTH -1;
                vertices[id++] = 2.f*(HEIGHT-j-1)/HEIGHT-1;
                vertices[id++] = 0;
            }
        }
        mesh.setVertices(vertices);
    }

    private void setCamera(){
        camera.position.set(10,0,0);
        camera.lookAt(0,0,0);
        camera.far=100f;
        camera.near=0.1f;

        //camera.up.set(0,0,1);
        camera.update();

        cameraLogg();
        setMesh();
    }

    private ShaderProgram setLocations(){
        ShGenerator generator= new ShGenerator();

        ShColor color = generator.addUniform(new ShColor());
        color.set(Color.DARK_GRAY);
        uniforms.add(color);

        ShLight light = generator.addUniform(new ShLight());
        light.position.set(4,4,4);
        light.color.set(Color.YELLOW);
        uniforms.add(light);

        generator.addUniform(camera);
        uniforms.add(camera);

        ShPlane plane = generator.addUniform(new ShPlane());
        plane.position.set(0,-2,0);
        plane.nor.set(0,-1,0);
        uniforms.add(plane);

//        ShBox box = generator.addUniform(new ShBox());
//        box.position.set(0,0,2);
//        uniforms.add(box);

//        ShTorus torus = generator.addUniform(new ShTorus());
//        torus.position.set(0,0,0);
//        uniforms.add(torus);

        //ShFractal fractal=generator.addUniform(new ShFractal());
        //uniforms.add(fractal);

        for (int i=0;i<1;i++) {
            ShSphere sphere = new ShSphere();
            generator.addUniform(sphere);
            uniforms.add(sphere);
            sphere.position.x+=2*i;
            sphere.r=1+i/2f;
        }

        ShaderProgram shaderProgram = new ShaderProgram(
                ShGenerator.getVertex(),
                generator.build());
        if (!shaderProgram.isCompiled())throw new GdxRuntimeException(shaderProgram.getLog());
        for (int i = 0; i <uniforms.size; i++) uniforms.get(i).setLocation(shaderProgram);
        return shaderProgram;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);
        ShaderProgram.pedantic = false;
        shader = setLocations();
        //shader = new ShaderProgram(Gdx.files.internal("shaders/sh1.vertex.glsl"),Gdx.files.internal("shaders/sh1.fragment.glsl"));
        setCamera();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))Gdx.app.exit();
        controller.update();
        System.out.println(1/delta);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shader.begin();
        for (int i=0;i<uniforms.size;i++) uniforms.get(i).setUniform(shader);
        mesh.render(shader, GL20.GL_POINTS);
        shader.end();

        /*
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.GREEN);
        renderer.line(0,0,0,1000,0,0);
        renderer.setColor(Color.RED);
        renderer.line(0,0,0,0,1000,0);
        renderer.setColor(Color.BLUE);
        renderer.line(0,0,0,0,0,1000);
        //renderer.setColor(Color.WHITE);
        //renderer.box(-1,-1,-1,2,2,-2);
        //renderer.setColor(Color.BLACK);
        //renderer.cone(0,0,0,1,1);
        renderer.end();

         */

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
