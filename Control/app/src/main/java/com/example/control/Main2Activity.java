package com.example.control;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main2Activity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;

    private Button signOut;
    private TextView tv_iluminacion, tv_puerta, tv_movimiento, tv_temperatura;
    private SeekBar sk_red, sk_green, sk_blue;
    private SwitchCompat rele1,rele2;
    private Integer sk1, sk2, sk3;
    private ProgressBar pgiluminacion, pgtemperatura, pgpresencia, pgmovimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        signOut = (Button)findViewById(R.id.so_btn);
        tv_iluminacion = (TextView)findViewById(R.id.TViluminacion);
        tv_movimiento = (TextView)findViewById(R.id.TVmovimiento);
        tv_puerta = (TextView)findViewById(R.id.TVpuerta);
        tv_temperatura = (TextView)findViewById(R.id.TVtemperatura);
        sk_red = (SeekBar)findViewById(R.id.seekBarRed);
        sk_green = (SeekBar)findViewById(R.id.seekBarGreen);
        sk_blue = (SeekBar)findViewById(R.id.seekBarBlue);
        rele1 = (SwitchCompat)findViewById(R.id.toggleButton);
        rele2 = (SwitchCompat)findViewById(R.id.toggleButton2);

        pgiluminacion = (ProgressBar)findViewById(R.id.progressBar);
        pgtemperatura = (ProgressBar)findViewById(R.id.progressBar1);
        pgmovimiento = (ProgressBar)findViewById(R.id.progressBar2);
        pgpresencia = (ProgressBar)findViewById(R.id.progressBar3);



        sk_red.setMax(255);
        sk_green.setMax(255);
        sk_blue.setMax(255);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        myRef.child("temperatura").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tv_temperatura.setText(""+dataSnapshot.getValue()+ " °C");
                pgtemperatura.setProgress(Integer.parseInt(dataSnapshot.getValue().toString()));
                if(Integer.parseInt(dataSnapshot.getValue().toString()) <= 18){
                    notification("Temperatura: " + dataSnapshot.getValue()+"°","Temperatura estable");
                }else if(Integer.parseInt(dataSnapshot.getValue().toString()) <= 28){
                    notification("Temperatura: " + dataSnapshot.getValue()+"°", "Temperatura subiendo");
                }else if(Integer.parseInt(dataSnapshot.getValue().toString()) >= 34){
                    notification("Temperatura: " + dataSnapshot.getValue()+"°","Temperatura alta");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef.child("iluminacion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tv_iluminacion.setText(""+dataSnapshot.getValue()+ "%");
                pgiluminacion.setProgress(Integer.parseInt(dataSnapshot.getValue().toString()));

                if (Integer.parseInt(dataSnapshot.getValue().toString()) <= 10){
                    notification("Nivel de luz: " + dataSnapshot.getValue()+"%", "Se encenderán las luces programadas");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef.child("puerta").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString() == String.valueOf(1)){
                    tv_puerta.setText(""+dataSnapshot.getValue());
                    notification("¡ALERTA!", "Se ha abierto la puerta, revisa la actividad");
                    pgpresencia.setProgress(100);
                }else{
                    tv_puerta.setText(""+dataSnapshot.getValue());
                    pgpresencia.setProgress(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef.child("movimiento").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString() == String.valueOf(1)){
                    tv_movimiento.setText(""+dataSnapshot.getValue());
                    pgmovimiento.setProgress(100);
                    notification("Movimiento","¡Se ha detectado un movimiento!");
                }else{
                    tv_movimiento.setText(""+dataSnapshot.getValue());
                    pgmovimiento.setProgress(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.child("rele1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (Integer.valueOf(dataSnapshot.getValue().toString()) == 0){
                    rele1.setChecked(false);
                }else{
                    rele1.setChecked(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef.child("rele2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (Integer.valueOf(dataSnapshot.getValue().toString()) == 0){
                    rele2.setChecked(false);
                }else{
                    rele2.setChecked(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef.child("rojo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sk_red.setProgress(Integer.valueOf(dataSnapshot.getValue().toString()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef.child("verde").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sk_green.setProgress(Integer.valueOf(dataSnapshot.getValue().toString()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef.child("azul").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sk_blue.setProgress(Integer.valueOf(dataSnapshot.getValue().toString()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rele1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rele1.isChecked()){
                    myRef.child("rele1").setValue(1);
                }else{
                    myRef.child("rele1").setValue(0);
                }
            }
        });
        rele2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rele2.isChecked()){
                    myRef.child("rele2").setValue(1);
                }else{
                    myRef.child("rele2").setValue(0);
                }
            }
        });
        sk_red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sk1 = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myRef.child("rojo").setValue(sk1);
            }
        });
        sk_green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sk2 = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myRef.child("verde").setValue(sk2);
            }
        });
        sk_blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sk3 = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myRef.child("azul").setValue(sk3);
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               MainActivity.mAuth.signOut();
               Intent i = new Intent(Main2Activity.this, MainActivity.class);
               startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        MainActivity.mAuth.signOut();
        Intent i = new Intent(Main2Activity.this, MainActivity.class);
        startActivity(i);
    }
    public String[] notification(String title, String status){
        String[] args = new String[2];
        args[0] = title;
        args[1] = status;

        Intent intent = new Intent(Main2Activity.this, Main2Activity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(Main2Activity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(Main2Activity.this);
            b.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_alert)
                    .setTicker("Atento a tu hogar")
                    .setContentTitle(args[0])
                    .setContentText(args[1])
                    .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                    .setContentIntent(contentIntent)
                    .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                    .setContentInfo("Info");
            NotificationManager notificationManager = (NotificationManager) Main2Activity.this.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, b.build());

            return args;
    }
}