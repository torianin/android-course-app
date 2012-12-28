package com.torianin.androidapp10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class OpenGLActivity extends Activity
{
    private GLSurfaceView sv;  // powierzchnia rysowania
    
    /** Called when the activity is first created. */
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        sv = new GLSurfaceView(this);
        sv.setRenderer(new OpenGLRenderer());  // ustalenie obiektu kontroluj¹cego rysowanie (tzw. renderer)
        // odrysowanie tylko w razie potrzeby (nale¿y w³¹czyæ dla grafiki statycznej):
        sv.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setContentView(sv);  // zawartoœæ widoku to powierzchnia rysowania
    }
    
    @Override
	public void onPause()
    {
    super.onPause();
        sv.onPause(); 
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        sv.onResume();
    }
}
