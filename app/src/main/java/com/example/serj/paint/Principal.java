package com.example.serj.paint;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.serj.paint.colorpicker.ColorPicker;
import com.example.serj.paint.colorpicker.ColorPickerDialog;

public class Principal extends Activity{

    private Vista vista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vista = new Vista(this);
        setContentView(vista);
        vista.setPincel(1);
        vista.setPincelColor(Color.BLACK);
        vista.setPincelGrosor(5);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.brush: {
                setupPincel();
            }
            case R.id.eraser: {
                vista.setBorrador();
                vista.setPincel(1);
                return true;
            }
            case R.id.shape: {
                return chooseShape();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean setupPincel(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View pincel = inflater.inflate(R.layout.dialog_pincel, null);
        builder.setTitle("Tipo de pincel");
        builder.setView(pincel);
        final ColorPicker cp = (ColorPicker)pincel.findViewById(R.id.colorPicker);
        cp.setColor(vista.getColor());
        final SeekBar sb = (SeekBar)pincel.findViewById(R.id.seekBar);
        sb.setProgress(vista.getGrosor());
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                vista.setPincelColor(cp.getColor());
                vista.setPincelGrosor(sb.getProgress());
            }
        });
        builder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    private boolean chooseShape(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View forma = inflater.inflate(R.layout.dialog_shapes, null);
        builder.setTitle("Tipo de forma");
        builder.setView(forma);
        final RadioButton rb1 = (RadioButton)forma.findViewById(R.id.rbLinea);
        final RadioButton rb2 = (RadioButton)forma.findViewById(R.id.rbRectangulo);
        final RadioButton rb3 = (RadioButton)forma.findViewById(R.id.rbOval);
        int pincel = vista.getPinceles();
        if( pincel > 1){
            if(pincel == 2) {
                rb1.setChecked(true);
            } else if (pincel == 3){
                rb2.setChecked(true);
            } else if (pincel == 4) {
                rb3.setChecked(true);
            }
        }
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(rb1.isChecked()){
                    vista.setPincel(2);
                }else if(rb2.isChecked()){
                    vista.setPincel(3);
                }else if(rb3.isChecked()){
                    vista.setPincel(4);
                }

            }
        });
        builder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }
}
