package com.light;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class Start extends Game {
    @Override
    public void create() {
        setScreen(new RayMarchingMode());
    }
}
