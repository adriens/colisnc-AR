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


import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.rendering.ModelRenderable;

import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.io.Serializable;



public class FirstView extends AppCompatActivity implements Serializable {


//    Session session = new Session(this);
//    Config config = new Config(session);

    private ArFragment arFragment;
    private ModelRenderable andyRenderable;

    // a enlever si on passe directement par le bouton next et non par la détection d'un codebarre
    private String resultat;






    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(getIntent().hasExtra("resultat")){
            String resultat = (String) getIntent().getSerializableExtra("resultat");
        }
        else {
            resultat = "AUCUN RESULTAT";
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_renderable);
        System.out.println("ON FIRST VIEW : resultat ==> "+resultat);

        Stockage stockage = new Stockage(resultat);
        stockage.getInfo();




        createArView();



    }



    public void createArView(){


//        Config ar_session_config = session.getConfig();
//        ar_session_config.setFocusMode(Config.FocusMode.AUTO);
//        session.configure(ar_session_config);

        System.out.println("DANS CREATEArVIEW\n\n----------------------------------------------------------");
        // Si on veut cacher le logo qui nous dis comment bouger notre tél
//        arFragment.planeDiscoveryController.hide()
//        arFragment.planeDiscoveryController.setInstructionView(null)

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        ModelRenderable.builder()
                .setSource(this, R.raw.mascot_v2)
                .build()
                .thenAccept(renderable -> andyRenderable = renderable)
        ;


        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    System.out.println("DANS on tap listener\n\n----------------------------------------------------------");

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
