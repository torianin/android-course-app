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
    private int st = 1;
    
    public float[] srodek(float x1, float y1, float x2, float y2){
		float srodek[] = {
		(x1+x2)/2,
		(y1+y2)/2
		};
		return srodek;
    }
    
    public void rysuj_trójk¹t(GL10 gl) // definicja pojedynczego trójk¹ta
    {
    	float points[] =  // kolejnoœæ wierzcho³ków przeciwna do ruchu wskazówek zegara
    	    {                 // uk³ad wspó³rzêdnych prawoskrêtny
    	        -1,-1, 0,  // 0
    	         0, 1, 0,  // 1
    	         1, -1, 0   // 2
    	};  // wspó³rzêdne punktów w tablicy Java
        short indeksy[] = { 0, 1, 2 };  // indeksy punktów w tablicy Java
        ByteBuffer vbuf = ByteBuffer.allocateDirect(VERTICES*3*4);  // 3 wspó³rzêdne * 4 bajty (float)
        vbuf.order(ByteOrder.nativeOrder());
        vb = vbuf.asFloatBuffer();  // tworzenie bufora wierzcho³ków
        vb.put(points);             // na podstawie tablicy wierzcho³ków
        vb.position(0);
        ByteBuffer ibuf = ByteBuffer.allocateDirect(INDEXES*2);  // * 2 bajty (short)
        ibuf.order(ByteOrder.nativeOrder());
        ib = ibuf.asShortBuffer();  // tworzenie bufora indeksów
        ib.put(indeksy);            // na podstawie tablicy indeksów
        ib.position(0);
        gl.glColor4f(1.0f, 0.0f, 0.0f, 0.5f); // kolor rysowania czerwony (model koloru RGBA)
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vb); // bufor wierzcho³ków
        gl.glDrawElements(GL10.GL_TRIANGLES, INDEXES, GL10.GL_UNSIGNED_SHORT, ib); // rys. z bufora indeksów
        if (st>0){
        sierpinski(gl,points,st);
        }
    }
    
    public void sierpinski(GL10 gl,float points[],int st){ // definicja rekurencyjnego trójk¹ta
    	float newpoints[] =  // kolejnoœæ wierzcho³ków przeciwna do ruchu wskazówek zegara
        {                 // uk³ad wspó³rzêdnych prawoskrêtny
        	 srodek(points[0],points[1],points[3],points[4])[0],srodek(points[0],points[1],points[3],points[4])[1], 0,  // 0
        	 srodek(points[3],points[4],points[6],points[7])[0],srodek(points[3],points[4],points[6],points[7])[1], 0,  // 1
        	 srodek(points[6],points[7],points[0],points[1])[0],srodek(points[6],points[7],points[0],points[1])[1], 0  // 2
        };  // wspó³rzêdne punktów w tablicy Java
        
        short indeksy[] = { 0, 1, 2 };  // indeksy punktów w tablicy Java
        ByteBuffer vbuf = ByteBuffer.allocateDirect(VERTICES*3*4);  // 3 wspó³rzêdne * 4 bajty (float)
        vbuf.order(ByteOrder.nativeOrder());
        vb = vbuf.asFloatBuffer();  // tworzenie bufora wierzcho³ków
        vb.put(newpoints);             // na podstawie tablicy wierzcho³ków
        vb.position(0);
        ByteBuffer ibuf = ByteBuffer.allocateDirect(INDEXES*2);  // * 2 bajty (short)
        ibuf.order(ByteOrder.nativeOrder());
        ib = ibuf.asShortBuffer();  // tworzenie bufora indeksów
        ib.put(indeksy);            // na podstawie tablicy indeksów
        ib.position(0);
        gl.glColor4f(0.5f, 0.5f, 0.5f, 1.0f); // kolor rysowania czerwony (model koloru RGBA)
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vb); // bufor wierzcho³ków
        gl.glDrawElements(GL10.GL_TRIANGLES, INDEXES, GL10.GL_UNSIGNED_SHORT, ib); // rys. z bufora indeksów
        if (st>1 ){
        float generatenew[] = {
        		points[0],points[1],0,
        		newpoints[0],newpoints[1],0,
        		newpoints[6],newpoints[7],0,
        };
        sierpinski( gl, generatenew , st-1);
        
        float generatenew2[] = {
        		newpoints[0],newpoints[1],0,
        		points[3],points[4],0,
        		newpoints[3],newpoints[4],0,
        };
        sierpinski( gl, generatenew2 , st-1);
        
        float generatenew3[] = {
        		newpoints[6],newpoints[7],0,
        		newpoints[3],newpoints[4],0,
        		points[6],points[7],0,
        };
        sierpinski( gl, generatenew3 , st-1);
        }
    }
    

    
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig)
    {   // metoda wywo³ywana tylko raz do utworzenia powierzchni rysowania
        gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);      // kolor t³a szary (model koloru RGBA)
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
        rysuj_trójk¹t(gl);
    }
}