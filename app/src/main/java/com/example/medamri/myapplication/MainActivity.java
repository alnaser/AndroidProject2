package com.example.medamri.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    private String url="http://factorycampus.net/services/properties.php";
    private ProgressDialog pDialog;
    private HashMap<String, String> infoSms=null;
    TextView smsTxtView;
    TextView formatTxtView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button send = (Button)findViewById(R.id.button);
        smsTxtView= (TextView) findViewById(R.id.smsTextView);
        formatTxtView= (TextView) findViewById(R.id.formatTexetView);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetContacts().execute();
            }
        });

    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response

            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);



                    String value =jsonObj.getString("sms");
                    Log.d("Data Json sms",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> SMS "+value);
                    infoSms = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    infoSms.put("SMS", jsonObj.getString("sms")!=null?jsonObj.getString("sms"):"null");
                    infoSms.put("FORMAT", jsonObj.getString("format")!=null?jsonObj.getString("format"):"null");
                    infoSms.put("ACTIVE", jsonObj.getString("active")!=null?jsonObj.getString("active"):"null");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            smsTxtView.setText(infoSms.get("SMS"));
            formatTxtView.setText(infoSms.get("FORMAT"));

        }

    }



}
