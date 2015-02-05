package com.example.serj.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class Vista extends View {

    private Paint pincel;
    private Canvas lienzoFondo;
    private Bitmap mapaDeBits;
    private int ancho, alto;
    private Path path = new Path();
    private int grosor;
    private int color;
    private final int backgroundColor = Color.WHITE;
    private enum Pinceles {
        PINCEL,
        LINEA,
        RECTANGULO,
        CIRCULO
    }
    private Pinceles pinceles;
    private float x0=0, y0=0, xi=0, yi=0;

    public Vista(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
    }

    private void setupPaint() {
        pincel = new Paint();
        pincel.setColor(color);
        pincel.setAntiAlias(true);
        pincel.setStrokeWidth(grosor);
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeJoin(Paint.Join.ROUND);
        pincel.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(backgroundColor); // Poner el fondo blanco
        canvas.drawBitmap(mapaDeBits, 0, 0, null);
        switch (pinceles) {
            case PINCEL:
                canvas.drawPath(path, pincel);
                break;
            case LINEA:
                canvas.drawLine(x0, y0, xi, yi, pincel);
                break;
            case RECTANGULO:
                canvas.drawRect(Math.min(x0, xi),
                        Math.min(y0,yi),
                        Math.max(x0, xi),
                        Math.max(y0, yi),
                        pincel);
                break;
            case CIRCULO:
                canvas.drawOval(new RectF(Math.min(x0, xi),
                                Math.min(y0,yi),
                                Math.max(x0, xi),
                                Math.max(y0, yi)),
                        pincel);
                break;
            default: break;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int evento = event.getAction();

        switch (pinceles) {
            case PINCEL:
                Log.v("drawPincel", "do da pinsel");
                return drawPincel(x, y, evento);
            case LINEA:
                Log.v("drawLinea", "do da linea");
                return drawLinea(x, y, evento);
            case RECTANGULO:
                Log.v("drawRect", "do da rect");
                return drawRectangulo(x, y, evento);
            case CIRCULO:
                Log.v("drawOval", "do da oval");
                return drawCirculo(x, y, evento);
            default:
                return false;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mapaDeBits = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        lienzoFondo = new Canvas(mapaDeBits);
        alto = h;
        ancho = w;
    }

    private boolean drawPincel(float x, float y, int event) {
        switch (event) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                lienzoFondo.drawPath(path, pincel); // Guardar el dibujo en el canvas
                path.reset();
                invalidate();
                break;
            default:
                return false;
        }
        postInvalidate();
        return true;
    }

    private boolean drawLinea(float x, float y, int event) {
        switch (event) {
            case MotionEvent.ACTION_DOWN:
                x0 = x;
                y0 = y;
                path.reset();
                path.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                xi = x;
                yi = y;
                path.quadTo(xi, yi, (x + xi) / 2, (y + yi) / 2);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                xi = x;
                yi = y;
                lienzoFondo.drawLine(x0, y0, xi, yi, pincel); // Guardar el dibujo en el canvas
                path.reset();
                invalidate();
                break;
        }
        return true;
    }

    private boolean drawRectangulo(float x, float y, int event) {
        switch (event) {
            case MotionEvent.ACTION_DOWN:
                x0 = x;
                y0 = y;
                path.reset();
                path.moveTo(x0, y0);
                break;
            case MotionEvent.ACTION_MOVE:
                xi = x;
                yi = y;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                xi = x;
                yi = y;
                lienzoFondo.drawRect(Math.min(x0, xi),
                        Math.min(y0,yi),
                        Math.max(x0, xi),
                        Math.max(y0, yi),
                        pincel); // Guardar el dibujo en el canvas
                path.reset();
                invalidate();
                x0=y0=xi=yi=-1; // Reset coordenadas
                break;
        }
        return true;
    }

    private boolean drawCirculo(float x, float y, int event) {
        switch (event) {
            case MotionEvent.ACTION_DOWN:
                x0 = x;
                y0 = y;
                path.reset();
                path.moveTo(x0, y0);
                break;
            case MotionEvent.ACTION_MOVE:
                xi = x;
                yi = y;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                xi = x;
                yi = y;
                lienzoFondo.drawOval(new RectF(Math.min(x0, xi),
                                Math.min(y0,yi),
                                Math.max(x0, xi),
                                Math.max(y0, yi)),
                        pincel); // Guardar el dibujo en el canvas
                path.reset();
                invalidate();
                x0=y0=xi=yi=-1; // Reset coordenadas
                break;
        }
        return true;
    }

    public void setPincelColor(int color) {
        this.color = color;
        pincel.setColor(color);
    }

    public void setPincelGrosor(int grosor) {
        this.grosor = grosor;
        pincel.setStrokeWidth(grosor);
    }

    public void setBorrador(){
        pincel.setColor(backgroundColor);
    }

    public void setPincel(int p) {
        if(p == 1){
            this.pinceles = Pinceles.PINCEL;
        } else if(p == 2) {
            this.pinceles = Pinceles.LINEA;
        } else if(p == 3) {
            this.pinceles = Pinceles.RECTANGULO;
        } else if(p == 4) {
            this.pinceles = Pinceles.CIRCULO;
        }
    }

    public int getPinceles() {
        if(pinceles == Pinceles.PINCEL){
            return 1;
        } else if(pinceles == Pinceles.LINEA){
            return 2;
        } else if(pinceles == Pinceles.RECTANGULO) {
            return 3;
        } else if(pinceles == Pinceles.CIRCULO) {
            return 4;
        } else {
            return -1;
        }
    }

    public int getColor() {
        return color;
    }

    public int getGrosor() {
        return grosor;
    }
}
