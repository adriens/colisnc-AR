package com.example.myfirstapp;


import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

//



public class FirstView extends AppCompatActivity implements Serializable {


//    Session session = new Session(this);
//    Config config = new Config(session);

    private ArFragment arFragment;
    private ModelRenderable andyRenderable;
    private ViewRenderable testViewRenderable;
    private TextView textView = (TextView) findViewById(R.id.card);
    // a enlever si on passe directement par le bouton next et non par la détection d'un codebarre
    private String resultat;
    private String response;





    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_renderable);

        //getInformation();


        createArView();


    }

    public void getInformation(){
        String response = "resultat par defaut";
        if(getIntent().hasExtra("resultat")){
            resultat = getIntent().getStringExtra("resultat");
        }
        else {
            resultat = "AUCUN RESULTAT";
        }
        System.out.println("ON FIRST VIEW : resultat ==> "+resultat);
        Stockage stockage = new Stockage(resultat);

        try {
            response = stockage.getInfo();
        } catch (UnirestException e) {
            System.err.println("erreur unirest lors de l'appel de get info");
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        System.out.println("Response : "+response+"\n\n");
        try {
            jsonArray = new JSONArray(response);
            //jsonObject = new JSONObject(response);

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (NullPointerException n){
            n.printStackTrace();
        }

        try {
            System.out.println("\n\nResponse jsonArray: "+jsonArray.getJSONObject(1).get("pays")+"\n\n");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        System.out.println("\n\nResponse : "+response+"\n\n");
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

/////////////////////////////////////////////////////////////////////////////////////////////
        textView.setText("VALEUR MODIFIEE");
        textView.setTextColor(16711800);
        ViewRenderable.builder()
                .setView(this, R.layout.view_renderable_textview)
                .build()
                .thenAccept(renderable -> testViewRenderable = renderable);

/////////////////////////////////////////////////////////////////////////////////////////////
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


                    //on attache notre textView
                    TransformableNode Node2 = new TransformableNode(arFragment.getTransformationSystem());
                    Node2.setParent(anchorNode);
                    Node2.setRenderable(testViewRenderable);

                    Node2.select();

                });

    }




}
