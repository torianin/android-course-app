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
    private final static int VERTICES = 3;  // liczba wierzcho³ków
    private final static int INDEXES = 3;   // liczba indeksów
    private FloatBuffer vb;  // bufor wierzcho³ków (vertex buffer)
    private ShortBuffer ib;  // bufor indeksów (index buffer)
    
    public void definiuj_obiekt() // definicja pojedynczego trójk¹ta
    {
        float punkty[] =  // kolejnoœæ wierzcho³ków przeciwna do ruchu wskazówek zegara
        {                 // uk³ad wspó³rzêdnych prawoskrêtny
            -0.5f,  0.5f, 0,  // 0
             0.5f, -0.5f, 0,  // 1
             0.5f,  0.5f, 0   // 2
        };  // wspó³rzêdne punktów w tablicy Java
        short indeksy[] = { 0, 1, 2 };  // indeksy punktów w tablicy Java
        ByteBuffer vbuf = ByteBuffer.allocateDirect(VERTICES*3*4);  // 3 wspó³rzêdne * 4 bajty (float)
        vbuf.order(ByteOrder.nativeOrder());
        vb = vbuf.asFloatBuffer();  // tworzenie bufora wierzcho³ków
        vb.put(punkty);             // na podstawie tablicy wierzcho³ków
        vb.position(0);
        ByteBuffer ibuf = ByteBuffer.allocateDirect(INDEXES*2);  // * 2 bajty (short)
        ibuf.order(ByteOrder.nativeOrder());
        ib = ibuf.asShortBuffer();  // tworzenie bufora indeksów
        ib.put(indeksy);            // na podstawie tablicy indeksów
        ib.position(0);
    }
    
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig)
    {   // metoda wywo³ywana tylko raz do utworzenia powierzchni rysowania
        gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);      // kolor t³a szary (model koloru RGBA)
        definiuj_obiekt();
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY); // w³¹czone u¿ycie bufora indeksów
    }
    
    public void onSurfaceChanged(GL10 gl, int szer, int wys)
    {   // metoda wywo³ywana automat. po zmianie wymiarów pow. rysowania (np. zmiana orientacji urz¹dzenia)
        gl.glViewport(0, 0, szer, wys);      // parametry ekranu (p³aszczyzny rzutowania)
        float aspekt = (float) szer/wys;     // proporcje ekranu
        gl.glMatrixMode(GL10.GL_PROJECTION); // tryb macierzy rzutowania
        gl.glLoadIdentity();                 // ustawienie macierzy jednostkowej (stan domyœlny)
        gl.glFrustumf(-aspekt, aspekt, -1, 1, 3, 7); // macierz rzutowania
    }
    
    public void onDrawFrame(GL10 gl)
    {   // metoda wywo³ywana automat. w przypadku koniecznoœci odrysowania zawartoœci powierzchni rysowania
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); // wyczyszczenie pow. rysowania
        gl.glMatrixMode(GL10.GL_MODELVIEW); // tryb macierzy MODELVIEW
        gl.glLoadIdentity();                // ustawienie macierzy jednostkowej (stan domyœlny)
        GLU.gluLookAt(gl, 0, 0, 5, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f); // parametry kamery
        gl.glColor4f(0.0f, 1.0f, 0.0f, 0.5f); // kolor rysowania zielony (model koloru RGBA)
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vb); // bufor wierzcho³ków
        gl.glDrawElements(GL10.GL_TRIANGLES, INDEXES, GL10.GL_UNSIGNED_SHORT, ib); // rys. z bufora indeksów
    }
}