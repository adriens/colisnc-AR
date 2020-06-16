package com.example.myfirstapp;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.util.ArrayList;

//



public class FirstView extends AppCompatActivity implements Serializable {


//    Session session = new Session(this);
//    Config config = new Config(session);

    private TextView textView;
    private JSONArray msg;



    private ArFragment arFragment;
    private ModelRenderable andyRenderable;
    private ViewRenderable testViewRenderable;

    // a enlever si on passe directement par le bouton next et non par la détection d'un codebarre
    private String resultat;
    private String response;

    ListView lvInfo;
    String[] finalInformation;

    String[] essais;


    private GestureDetector trackableGestureDetector;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_renderable);


        this.msg=getInformation();



        createArView();


    }

    public JSONArray getInformation(){
        //Organise le résultat que nous donne l'API
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
        return jsonArray;
    }

    public String[] getData(){
       ArrayList<String> res = new ArrayList<>();
        for(int i=0; i<this.msg.length(); i++){
            String tmp = "";
            try {
                tmp = tmp.concat(this.msg.getJSONObject(i).get("typeEvenement")+"\n");
                String[] HeureDate = this.msg.getJSONObject(i).get("rawDateHeure").toString().split(" ");
                tmp = tmp.concat(HeureDate[0] + " à " + HeureDate[1]+"\n");
                tmp = tmp.concat("(" + this.msg.getJSONObject(i).getJSONObject("country").get("iso") + ") ").concat(this.msg.getJSONObject(i).get("localisation")+"\n");

//                tmp = tmp + (this.msg.getJSONObject(i).get("typeEvenement")+"\n");
//                String[] HeureDate = this.msg.getJSONObject(i).get("rawDateHeure").toString().split(" ");
//                tmp = tmp +(HeureDate[0] + " à " + HeureDate[1]+"\n");
//                tmp = tmp +("(" + this.msg.getJSONObject(i).getJSONObject("country").get("iso") + ") ");
//                tmp = tmp +(this.msg.getJSONObject(i).get("localisation")+"\n");
                try {
                    if (!this.msg.getJSONObject(i).getJSONObject("localization").get("url").toString().equals("null")) {
//                        A REJOUTER POUR AVOIR L'UR DE LA LOCALSATION
//                        tmp = tmp.concat((String)this.msg.getJSONObject(i).getJSONObject("localization").get("url")+"\n");
                    }
                } catch (org.json.JSONException e) {
                    e.getCause();
                    e.getStackTrace();
                }

            }catch (JSONException jsonexeption){

            }
            res.add(tmp);
        }
        return res.toArray(new String[0]);
    }




    @SuppressLint("WrongConstant")
    public void createArView(){


//        Config ar_session_config = session.getConfig();
//        ar_session_config.setFocusMode(Config.FocusMode.AUTO);
//        session.configure(ar_session_config);

        System.out.println("DANS CREATEArVIEW\n\n----------------------------------------------------------");
        // Si on veut cacher le logo qui nous dis comment bouger notre tél
//        arFragment.planeDiscoveryController.hide()
//        arFragment.planeDiscoveryController.setInstructionView(null)

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);


        final LayoutInflater factory = getLayoutInflater();

        final View textEntryView = factory.inflate(R.layout.view_renderable_textview, null);
        lvInfo = (ListView) textEntryView.findViewById(R.id.card);

        finalInformation = getData();

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this,R.layout.list_item,finalInformation);

        lvInfo.setAdapter(monthAdapter);




        //OU BIEN FAIRE CA :


//        View myLayout = LayoutInflater.from(this).inflate(R.layout.MY_LAYOUT,null);
//
//            // load the text view
//        TextView myView = (TextView) myLayout.findViewById(R.id.MY_VIEW);
//
//


/////////////////////////////////////////////////////////////////////////////////////////////
//        textView.setText("VALEUR MODIFIEE AJDOEKODEK IEOJDEJDOEJDO doezjcjaezcijz cazopiuc hj zaopijcazopihjcazpoij cjazpoih chazpoic azpoiijcjazdpoijcazpopicopzijcz \b ciiazhecpuhazeopiucazeouphcazeoiuchazoucnazoucazepoiuc zdopuchadopicj azducuh z àçc azpoziijcj azddooijicdlj c daoiaj cazihjh cpdozia jcadlksh cadzodiih c aozij casdkjlnc azdopich azdoiicjadkljkncklsjn^çasd czadic !!!\b ##############################################");
//        textView.setText(this.msg.toString());
////        textView.setTextColor(0xffff0000);
////        textView.setAutoSizeTextTypeUniformWithConfiguration(5,15,1,1);
////        textView.setMovementMethod(new ScrollingMovementMethod());




        ViewRenderable.builder()
                .setView(this, textEntryView)
                .build()
                .thenAccept(renderable -> {
                    testViewRenderable = renderable;
//                    new Vector3(0.15f, 0.20f, 0.0f);
                });


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


                    Node.setOnTapListener((v,event) -> {
//                        v.getNode().setLocalRotation(Quaternion.axisAngle(new Vector3(1,1,1), (float) 3.2));
                        System.out.println("\n\n\nOMGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG\n\n\n");
                    });
                    



                    //on attache notre listView


                    anchorNode.setParent(arFragment.getArSceneView().getScene());
                    TransformableNode Node2 = new TransformableNode(arFragment.getTransformationSystem());
                    Node2.setParent(Node);
                    Node2.setLocalPosition(new Vector3(0.15f, 0.20f, -0.10f));
//                    Node2.setLookDirection(new Vector3(0.0f, 0.40f, 1.00f));
//                    Node2.setLocalPosition(new Vector3(2,2,2));
                    Node2.setRenderable(testViewRenderable);

                    Node2.select();

                });



    }

}
