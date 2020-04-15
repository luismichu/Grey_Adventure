package com.luismichu.greyadventure.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public final class MyShaderLoader {
	public static ShaderProgram createShader(String vertexName, String fragmentName){
		String vertexShader = Gdx.files.internal(
				"shaders/" + vertexName + ".glsl").readString();
		String fragmentShader = Gdx.files.internal(
				"shaders/" + fragmentName + ".glsl").readString();
		ShaderProgram.pedantic = false;
		ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);

		if (!shader.isCompiled()) {
			System.out.println(shader.getLog());
			Gdx.app.exit();
		}

		return shader;
	}
}
