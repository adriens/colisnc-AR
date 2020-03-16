package com.example.myfirstapp;





import android.os.Build;
import android.os.Bundle;


import android.view.MotionEvent;



import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;




import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;

import com.google.ar.sceneform.AnchorNode;



import com.google.ar.sceneform.rendering.ModelRenderable;

import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;



public class FirstView extends AppCompatActivity {

    private ArFragment arFragment;
    private ModelRenderable andyRenderable;






    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_renderable);
        createArView();



    }

    public void createArView(){

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        ModelRenderable.builder()
                .setSource(this, R.raw.mascot_v2)
                .build()
                .thenAccept(renderable -> andyRenderable = renderable)
        ;


        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (andyRenderable == null) {
                        return;
                    }
                    if (plane.getType() != Plane.Type.HORIZONTAL_UPWARD_FACING) {
                        return;
                    }


                    //On créé le point d'encrage du modèle 3d
                    Anchor anchor = hitResult.createAnchor();
                    System.out.println("ON ATTACHE NOTRE POINT D'ANCRAGE LA OU ON A CLIQUE\n\n");
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    //On attache ensuite notre modèle au point d'encrage
                    TransformableNode Node = new TransformableNode(arFragment.getTransformationSystem());
                    Node.setParent(anchorNode);
                    Node.setRenderable(andyRenderable);
                    System.out.println(andyRenderable);
                    Node.select();

                });

    }





}
