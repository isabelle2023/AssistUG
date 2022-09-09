package com.aplication.assistug.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.aplication.assistug.R;
import com.aplication.assistug.adapter.RvAsistenciaDias;
import com.aplication.assistug.model.AsistenciaDias;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class AsistenciaDiasActivity extends AppCompatActivity {
    String uidcurso, nombrecurso, semestre, tipouser;
    ImageButton ivbackasistenciadias;
    RecyclerView RvAsistenciaDias;
    RvAsistenciaDias rvAdapterAsistenciaDias;
    RecyclerView.LayoutManager lm;
    LinearLayout LLnodias;
    List<AsistenciaDias>  asistencia = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistencia_dias);
        LLnodias = findViewById(R.id.LLnodias);
        ivbackasistenciadias = findViewById(R.id.ivbackasistenciadias);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        uidcurso = getIntent().getStringExtra("uidcurso");
        nombrecurso = getIntent().getStringExtra("nombrecurso");
        semestre = getIntent().getStringExtra("semestre");
        tipouser = getIntent().getStringExtra("tipouser");

        ivbackasistenciadias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AsistenciaDiasActivity.this, InformacionCursoActivity.class);
                intent.putExtra("uidcurso" , uidcurso);
                intent.putExtra("nombrecurso" , nombrecurso);
                intent.putExtra("semestre", semestre);
                intent.putExtra("tipouser", tipouser);
                startActivity(intent);
                finish();
            }
        });



        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Cursos").child(uidcurso).child("asistencia");
        mDatabase.orderByChild("fecha").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                asistencia.clear();
                if(snapshot.exists()){
                    LLnodias.setVisibility(View.GONE);
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        AsistenciaDias asistenciaAlumno = dataSnapshot.getValue(AsistenciaDias.class);
                        asistencia.add(asistenciaAlumno);
                    }


                }
                rvAdapterAsistenciaDias.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Enlazamos la interfaz gráfica del RecyclerView con el código
        RvAsistenciaDias = findViewById(R.id.rv_asistenciadias);
        // Establecemos que los elementos del RecyclerView se apilen verticalmente
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        // Creamos un adaptador para el RecyclerView
        rvAdapterAsistenciaDias = new RvAsistenciaDias(this, asistencia,uidcurso, nombrecurso, tipouser);
        // Enlazamos el adaptador con el objeto RecyclerView
        RvAsistenciaDias.setAdapter(rvAdapterAsistenciaDias);
        RvAsistenciaDias.setLayoutManager(gridLayoutManager);
    }
}