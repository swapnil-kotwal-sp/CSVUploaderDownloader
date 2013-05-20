package com.test.csvuploaderdownloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVWriter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends Activity {

  private static Context MainActivity = null;
  private JSONArray dbJson = null;
  String act = "[{message:\"API Level:  12, OS version: 3.1, Device samsung-GT-P7500\",result:true,testcase:\"Device Info\"},{message:\"specTitle: Unknown vendor: Unknown virsion: 0.0\",result:true,testcase:\"Ooyala SDK Info\"},{message:\"----playerEmbedded----loadComplete----apiReady\",result:true,testcase:\"Player Init\"},{message:\"API Responses are Fetched Successfully\",result:true,testcase:\"API Response\"},{message:\"asset title: singing canary.mp4 type: video, Embedcode: M4ZnQ0NjqTnc2xDEkkVzJhfpgJz1TrHm\",result:true,testcase:\"1.Playback start\"}]";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    MainActivity = this;
    // Download and Save CSV data
    Button download = (Button) findViewById(R.id.btnDownload);
    download.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        try {
          JSONArray start = new JSONArray(act);
          saveCsv(start);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    // read CSV data.
    Button btnUpload = (Button) findViewById(R.id.btnUpload);
    btnUpload.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        try {
          String csv = readTXTFile(
              Environment.getExternalStorageDirectory().getAbsolutePath()
              + "/test/test1.csv").toString();
          JSONArray CSVToJson = new JSONArray(csv);
          dbJson = CSVToJson;
          Log.i("CSV Info", CSVToJson.toString());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

    /** database interaction **/
    // code to json to bean object
    Gson gson = new Gson();
    Type collectionType = new TypeToken<Collection<DataBean>>() {
    }.getType();
    Collection<DataBean> enums = gson.fromJson(act, collectionType);
    DataBean[] data = new Gson().fromJson(act, DataBean[].class);
    Log.i("JSON Bean", data.toString());
    Toast.makeText(this, enums.toString(), 10000).show();

    DatabaseHandler db = new DatabaseHandler(this);
    Log.d("Insert: ", "Inserting ..");
    db.addContact(new Contact("Ravi", "9100000000"));
    db.addContact(new Contact("Srinivas", "9199999999"));
    db.addContact(new Contact("Tommy", "9522222222"));
    db.addContact(new Contact("Karthik", "9533333333"));

    // Reading all contacts
    Log.d("Reading: ", "Reading all contacts..");
    final List<Contact> contacts = db.getAllContacts();
    Button btnSend = (Button) findViewById(R.id.btnSend);
    btnSend.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        showData(contacts);
      }
    });
    /** database interaction **/
  }

  // for database entrie fetch
  private void showData(List<Contact> contact) {
    StringBuilder sb = new StringBuilder();
    for (Contact cn : contact) {
      sb.append(" Id: " + cn.getID() + " ,Name: " + cn.getName() + " ,Phone: "
          + cn.getPhoneNumber());
    }
    Log.i("Name: ", sb.toString());
    Toast.makeText(this, sb.toString(), 10000).show();
  }

  public void saveCsv(JSONArray outerArray) throws IOException, JSONException {
    String rootPath = Environment.getExternalStorageDirectory()
    .getAbsolutePath() + "/test/";
    File dir = new File(rootPath);
    if (!dir.exists()) {
      dir.mkdir();
    }
    File file;
    EditText editText = (EditText) findViewById(R.id.editText1);
    if (!editText.getText().toString().equals("")) {
      file = new File(rootPath, editText.getText().toString() + ".csv");
    } else {
      editText.setError("Defualt csv file name will be used");

      file = new File(rootPath, "test1.csv");
    }
    if (!file.exists()) {
      file.createNewFile();
    }
    if (file.exists()) {
      CSVWriter writer = new CSVWriter(new FileWriter(file), ',');
      String[][] arrayOfArrays = new String[outerArray.length()][];
      for (int i = 0; i < outerArray.length(); i++) {
        JSONObject innerJsonArray = (JSONObject) outerArray.get(i);
        String[] stringArray1 = new String[innerJsonArray.length()];
        stringArray1[0] = (String) innerJsonArray.getString("testcase");
        stringArray1[1] = (String) innerJsonArray.getString("result");
        stringArray1[2] = (String) innerJsonArray.getString("message");
        arrayOfArrays[i] = stringArray1;
        writer.writeNext(arrayOfArrays[i]);
      }
      writer.close();
    }
  }

  private static List<List<String>> readTXTFile(String csvFileName)
  throws IOException {
    String line = null;
    BufferedReader stream = null;
    List<List<String>> csvData = new ArrayList<List<String>>();

    try {
      stream = new BufferedReader(new FileReader(csvFileName));
      while ((line = stream.readLine()) != null) {
        String[] splitted = line.split(",");
        List<String> dataLine = new ArrayList<String>(splitted.length);
        for (String data : splitted)
          dataLine.add(data);
        csvData.add(dataLine);
      }
    } finally {
      if (stream != null)
        stream.close();
    }
    Toast.makeText(MainActivity, csvData.toString(), 5000).show();
    return csvData;
  }
}
