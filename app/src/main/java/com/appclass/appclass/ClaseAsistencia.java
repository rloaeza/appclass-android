package com.appclass.appclass;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.appclass.appclass.db.Alumno;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ClaseAsistencia extends AppCompatActivity {

    Button bFecha;
    Button bBuscarBT;
    Button bCrearLista;
    Button bTerminar;
    ListView lvAlumnos;
    String claseCodigo;
    String claseNombre;

    AlumnoItemAdapter listaAlumnos;


    String fechaLista;


    private FirebaseDatabase firebaseDatabase ;
    private DatabaseReference databaseReference;

    private List<Alumno> listaAlumnosClase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clase_asistencia);

        claseCodigo = getIntent().getStringExtra(AppClassReferencias.claseCodigo);
        claseNombre = getIntent().getStringExtra(AppClassReferencias.claseNombre);

        setTitle(claseNombre);
        bFecha = findViewById(R.id.bFecha);
        bBuscarBT = findViewById(R.id.bBuscarBT);
        bTerminar = findViewById(R.id.bTerminar);
        lvAlumnos = findViewById(R.id.lvAlumnos);
        bCrearLista = findViewById(R.id.bCrearLista);

        listaAlumnos= new AlumnoItemAdapter(getApplicationContext(), new ArrayList<>());

        String correo = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        String correoFix=correo.replace(".", "+");

        fechaLista = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        bFecha.setText(fecha2Text());

        bFecha.setOnClickListener(e->{
            DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    fechaLista = year + "-" + (month<9?"0":"")+(month+1) + "-" + day;
                    bFecha.setText(fecha2Text());
                }
            });
            newFragment.show(getSupportFragmentManager(), "datePicker");
        } );


        lvAlumnos.setAdapter(listaAlumnos);



        bTerminar.setOnClickListener(e -> finish() );




        bCrearLista.setOnClickListener(e-> {




            databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Clases).child(claseCodigo).child(AppClassReferencias.Asistencias).child(fechaLista).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(Alumno alumno : listaAlumnosClase) {
                        alumno.setAsistio("0");
                        //listaAlumnos.add(alumno);

                        databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Clases).child(claseCodigo).child(AppClassReferencias.Asistencias).child(fechaLista).child(alumno.getId()).setValue(alumno);
                    }
                    bCrearLista.setVisibility(View.INVISIBLE);
                    bBuscarBT.setVisibility(View.VISIBLE);
                    bTerminar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });



        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(AppClassReferencias.AppClass);

        listaAlumnosClase = new ArrayList<>();

        ValueEventListener postListenerCargarAlumno = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaAlumnosClase.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Alumno alumno = item.getValue(Alumno.class);
                    listaAlumnosClase.add(alumno);
                }
                Toast.makeText(ClaseAsistencia.this, ""+listaAlumnosClase.size(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Clases).child(claseCodigo).child(AppClassReferencias.Alumnos).addValueEventListener(postListenerCargarAlumno);


        ValueEventListener postListenerCargarListaAsistencia = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    listaAlumnos.clear();
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        Alumno alumno = item.getValue(Alumno.class);
                        listaAlumnos.add(alumno);
                    }
                    Toast.makeText(ClaseAsistencia.this, "" + listaAlumnosClase.size(), Toast.LENGTH_SHORT).show();
                }
                else {
                    bCrearLista.setVisibility(View.VISIBLE);
                    bBuscarBT.setVisibility(View.INVISIBLE);
                    bTerminar.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Clases).child(claseCodigo).child(AppClassReferencias.Asistencias).child(fechaLista).addValueEventListener(postListenerCargarListaAsistencia);




    }

    private String fecha2Text() {
        Date fecha = Calendar.getInstance().getTime();
        try {
            fecha = new SimpleDateFormat("yyyy-MM-dd").parse(fechaLista);
        } catch (ParseException e1) { }
        return new SimpleDateFormat("dd / MMM / yyyy").format(fecha);
    }
}