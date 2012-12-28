package com.torianin.androidapp10;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

public class OpenGLRenderer implements GLSurfaceView.Renderer
{
    private final static int VERTICES = 3;  // liczba wierzcho�k�w
    private final static int INDEXES = 3;   // liczba indeks�w
    private FloatBuffer vb;  // bufor wierzcho�k�w (vertex buffer)
    private ShortBuffer ib;  // bufor indeks�w (index buffer)
    
    public void definiuj_obiekt() // definicja pojedynczego tr�jk�ta
    {
        float punkty[] =  // kolejno�� wierzcho�k�w przeciwna do ruchu wskaz�wek zegara
        {                 // uk�ad wsp�rz�dnych prawoskr�tny
            -0.5f,  0.5f, 0,  // 0
             0.5f, -0.5f, 0,  // 1
             0.5f,  0.5f, 0   // 2
        };  // wsp�rz�dne punkt�w w tablicy Java
        short indeksy[] = { 0, 1, 2 };  // indeksy punkt�w w tablicy Java
        ByteBuffer vbuf = ByteBuffer.allocateDirect(VERTICES*3*4);  // 3 wsp�rz�dne * 4 bajty (float)
        vbuf.order(ByteOrder.nativeOrder());
        vb = vbuf.asFloatBuffer();  // tworzenie bufora wierzcho�k�w
        vb.put(punkty);             // na podstawie tablicy wierzcho�k�w
        vb.position(0);
        ByteBuffer ibuf = ByteBuffer.allocateDirect(INDEXES*2);  // * 2 bajty (short)
        ibuf.order(ByteOrder.nativeOrder());
        ib = ibuf.asShortBuffer();  // tworzenie bufora indeks�w
        ib.put(indeksy);            // na podstawie tablicy indeks�w
        ib.position(0);
    }
    
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig)
    {   // metoda wywo�ywana tylko raz do utworzenia powierzchni rysowania
        gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);      // kolor t�a szary (model koloru RGBA)
        definiuj_obiekt();
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY); // w��czone u�ycie bufora indeks�w
    }
    
    public void onSurfaceChanged(GL10 gl, int szer, int wys)
    {   // metoda wywo�ywana automat. po zmianie wymiar�w pow. rysowania (np. zmiana orientacji urz�dzenia)
        gl.glViewport(0, 0, szer, wys);      // parametry ekranu (p�aszczyzny rzutowania)
        float aspekt = (float) szer/wys;     // proporcje ekranu
        gl.glMatrixMode(GL10.GL_PROJECTION); // tryb macierzy rzutowania
        gl.glLoadIdentity();                 // ustawienie macierzy jednostkowej (stan domy�lny)
        gl.glFrustumf(-aspekt, aspekt, -1, 1, 3, 7); // macierz rzutowania
    }
    
    public void onDrawFrame(GL10 gl)
    {   // metoda wywo�ywana automat. w przypadku konieczno�ci odrysowania zawarto�ci powierzchni rysowania
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); // wyczyszczenie pow. rysowania
        gl.glMatrixMode(GL10.GL_MODELVIEW); // tryb macierzy MODELVIEW
        gl.glLoadIdentity();                // ustawienie macierzy jednostkowej (stan domy�lny)
        GLU.gluLookAt(gl, 0, 0, 5, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f); // parametry kamery
        gl.glColor4f(0.0f, 1.0f, 0.0f, 0.5f); // kolor rysowania zielony (model koloru RGBA)
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vb); // bufor wierzcho�k�w
        gl.glDrawElements(GL10.GL_TRIANGLES, INDEXES, GL10.GL_UNSIGNED_SHORT, ib); // rys. z bufora indeks�w
    }
}