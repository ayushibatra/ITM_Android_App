/**
 * Copyright 2013 Bhavyanshu Parasher   
 * This file is part of "ITM University Android Application".
 * "ITM University Android Application" is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free Software Foundation, 
 * either version 3 of the License, or (at your option) any later version.
 * "ITM University Android Application" is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with "ITM University Android Application". 
 * If not, see http://www.gnu.org/licenses/.
 */

/**
 * This activity is responsible for adding data to the remote database. 
 * In this case, we are adding a question to talky.
 * POST method used! 
 */
package com.wbs.itm;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewQuestionActivity extends Activity {
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    JSONParser jsonParser = new JSONParser();
    EditText inputName;
    EditText inputDesc;
    private static String url_create_Question = "http://thewbs.getfreehosting.co.uk/talky/insques.php";
    private static final String TAG_SUCCESS = "success";
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ques);
 
        // Edit Text
        inputName = (EditText) findViewById(R.id.inputName);
        inputDesc = (EditText) findViewById(R.id.inputDesc);
        // Create button
        Button btnCreateQuestion = (Button) findViewById(R.id.btnCreateQues);
        btnCreateQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) 
            {
                new CreateNewQuestion().execute();
            }
        });
    }

    class CreateNewQuestion extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
           int len4= inputDesc.length();
            if (len4<=0) {
            	//AlertDialog alertDialog = new AlertDialog.Builder(NewQuestionActivity.this).create();
                inputDesc.setError(getText(R.string.error_required));
                //alertDialog.setMessage("Please don't leave it empty!");  
                //alertDialog.show();*/                    
                }
            else
            {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewQuestionActivity.this);
            pDialog.setMessage("Posting..please wait!");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            }
        }

        @Override
		protected String doInBackground(String... args) {
            String name = inputName.getText().toString();
            String description = inputDesc.getText().toString();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("question", description));
            JSONObject json = jsonParser.makeHttpRequest(url_create_Question,
                    "POST", params);
            Log.d("Create Response", json.toString());
            try {
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    Intent i = new Intent(getApplicationContext(), AllQuestionActivity.class);
                    startActivity(i);
                    finish();
                } else {
                	//Denote some failure
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
		protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }
 
    }
    
    @Override
    protected void onStart() {
       super.onStart();
       EasyTracker.getInstance().activityStart(this); // Add this method
    }
  
    @Override
    protected void onStop() {
       super.onStop();
       EasyTracker.getInstance().activityStop(this); // Add this method
    }
}