package com.example.myfirstapp.test;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

    public class Main {


        public static void main(String[] args) throws Exception {

            JSONArray infos = getInformation();

            System.out.println(infos + "\n");

            for(int i=0; i<infos.length(); i++){
                System.out.println(infos.getJSONObject(i).get("typeEvenement"));
                String[] HeureDate = infos.getJSONObject(i).get("rawDateHeure").toString().split(" ");
                System.out.println(HeureDate[0] + " à " + HeureDate[1]);
                System.out.print("(" + infos.getJSONObject(i).getJSONObject("country").get("iso") + ") ");
                System.out.println(infos.getJSONObject(i).get("localisation"));
                try{
                    if(!infos.getJSONObject(i).getJSONObject("localization").get("url").toString().equals("null")){
                        System.out.println(infos.getJSONObject(i).getJSONObject("localization").get("url"));
                    }
                }catch (org.json.JSONException e){
                    e.getCause();
                    e.getStackTrace();
                }
                System.out.println("------------------------------------------");

            }

        }

        public static JSONArray getInformation(){
            String response = "resultat par defaut";


            try {
                response = getInfo();
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


        public static String getInfo() throws  UnirestException {
            //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            //StrictMode.setThreadPolicy(policy);

            //System.out.println("CODE : "+this.code);
            HttpResponse<String> response;
            //RE053981424NC       CODE DE SECOURS
//        if(code.isEmpty()){
            response = Unirest.get("https://colis-nc.p.rapidapi.com/colis/RE053981424NC")
                    .header("x-rapidapi-host", "colis-nc.p.rapidapi.com")
                    .header("x-rapidapi-key", "f587dc6483msh84e28a7c9a650bcp11b260jsn7c8b7ea489f5")

                    .asString();
//        }else {
//
//            response = Unirest.get("https://colis-nc.p.rapidapi.com/colis/" + this.code)
//                    .header("x-rapidapi-host", "colis-nc.p.rapidapi.com")
//                    .header("x-rapidapi-key", "f587dc6483msh84e28a7c9a650bcp11b260jsn7c8b7ea489f5")
//
//                    .asString();
//        }
            //System.out.println(response.getBody().toString());
            return response.getBody();
        }
    }

