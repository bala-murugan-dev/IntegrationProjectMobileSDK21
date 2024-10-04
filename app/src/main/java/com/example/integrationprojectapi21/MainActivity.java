package com.example.integrationprojectapi21;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.integrationprojectapi21.util.CustomAdapter;
import com.example.mysdklib.Excal;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * MainActivity Class is an Activity that contains single input box,
 * By entering an URL for GET request (<a href="https://dog.ceo/api/breeds/image/random">...</a>) and pressing the button
 * will displays you json result in cardview
 */
public class MainActivity extends AppCompatActivity {
    CustomAdapter customAdapterObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //to track touch events
        //findViewById(R.id.main).setOnTouchListener(Excal.getInstance(null,null,0).getOnTouchListener()); // id -> of the layout

        // get the reference of RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<String> LoadingKeys = new ArrayList<>();
        LoadingKeys.add("No data");


        ArrayList<String> Loadingvals = new ArrayList<>();
        Loadingvals.add("...");



        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        customAdapterObj = new CustomAdapter(MainActivity.this, LoadingKeys, Loadingvals);
        recyclerView.setAdapter(customAdapterObj); // set the Adapter to RecyclerView


    }



    /**
     *clickButtonListener method is listening for button presses in the MainActivity
     * make a GET request to the URL and update the UI to show json response in cardview
     * and a toast of api21 app clicked will be appeared
     */
    public void clickButtonListener(View view) {
        Toast.makeText(this, "Api 21  " +
                "app  clicked", Toast.LENGTH_SHORT).show();
        EditText simpleEditText = (EditText) findViewById(R.id.simpleEditText);
        String editTextValue = simpleEditText.getText().toString();

//        Intent intent = new ShareCompat.IntentBuilder(this).setType("text/plain").setText("clipboard").setChooserTitle("share").createChooserIntent();
//        System.out.println("Resolver Pacakge name:"+intent.resolveActivity(getPackageManager()));
//        if(intent.resolveActivity(getPackageManager())!=null){
//            startActivity(intent);
//        }

        NetworkRequests.sendRequest(editTextValue,this);
    }
//    @Override
//    public boolean onTouchEvent(MotionEvent event)
//    {
//        if (event.getAction() == MotionEvent.ACTION_DOWN)
//        {
//            System.out.println("TOUCH ----");
//            sendBroadcast(new Intent("touch_event_has_occured"));
//        }
//
//        return super.onTouchEvent(event);
//    }
    public boolean dispatchTouchEvent(){
        return false;
    }

    public void execButtonListener(View view){
        Toast.makeText(this, "Api 21 command  " +
                "app  clicked", Toast.LENGTH_SHORT).show();
        EditText simpleEditText = (EditText) findViewById(R.id.simpleEditText);
        String editTextValue = simpleEditText.getText().toString();
        String output = executeCommand(editTextValue);
        updateListCommandExecution(editTextValue,output);

    }

    private boolean hasUsageStatsPermission() {
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    private void checkForegroundApp() {
        if (!hasUsageStatsPermission()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        long currentTime = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, currentTime - 1000 * 60, currentTime);

        if (usageStatsList != null && !usageStatsList.isEmpty()) {
            SortedMap<Long, UsageStats> sortedMap = new TreeMap<>();
            for (UsageStats usageStats : usageStatsList) {
                sortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                System.out.println(usageStats.getPackageName()+" = "+usageStats);
            }
            if (!sortedMap.isEmpty()) {
                String topPackageName = sortedMap.get(sortedMap.lastKey()).getPackageName();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    sortedMap.forEach((k,v)->{
                        System.out.println(k+":"+v.getPackageName());
                    });
                }
                System.out.println("Top package name"+topPackageName);
                // if top package name is whatsapp then it is definitely forwarded
            }
        }
    }

    public static String executeCommand(String command) {
        StringBuilder output = new StringBuilder();

        try {
            // Execute the command using Runtime
            Process process = Runtime.getRuntime().exec(command);

            // Read the output from the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Read the error stream (if any)
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Wait for the process to complete
            process.waitFor();
        } catch (Exception e) {
            return e.toString();
        }

        return output.toString();
    }

    /**
     *Update the View as CardView with json result from GET request
     */
    public void updateList(String jsonData){
        try{
            ArrayList<String> keys_list = new ArrayList<>();
            ArrayList<String> values_list = new ArrayList<>();
            JSONObject obj = new JSONObject(jsonData);
            for (Iterator<String> it = obj.keys(); it.hasNext(); ) {
                String key = it.next();
                String val = obj.getString(key);

                keys_list.add(key);
                values_list.add(val);

//                    Log.d("JSON","key = "+key+"val = "+val);
            }

            runOnUiThread(()->{
                this.customAdapterObj.onLoadedList(keys_list,values_list);
            });

        }catch (Exception e){
            Log.d("json",e.toString());
        }
    }
    public void updateListCommandExecution(String cmd,String out){

        ArrayList<String> keys_list = new ArrayList<>();
        ArrayList<String> values_list = new ArrayList<>();
       keys_list.add(cmd);
       values_list.add(out);

        runOnUiThread(()->{
            this.customAdapterObj.onLoadedList(keys_list,values_list);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        // check for forward
        checkForegroundApp();
    }
}