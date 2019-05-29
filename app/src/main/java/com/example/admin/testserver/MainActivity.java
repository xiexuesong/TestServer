package com.example.admin.testserver;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import comeastmoney.fileserver.utils.FileExtraUtils;
import comeastmoney.fileserver.utils.Utils;
import fi.iki.elonen.util.ServerRunner;


public class MainActivity extends Activity {


    private final int PORT = 8888;
    private final String DIR_NAME = "/eastmomey/fileServer";
    private String WEB_ROOT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button)findViewById(R.id.start_stop_button);
        btn.setOnClickListener(new ServiceBtnClickListen());
        onInitDir();
    }


    private void onInitDir(){
        String sdpath = FileExtraUtils.getSDPath();
        if(null == sdpath)
            this.WEB_ROOT = getApplicationContext().getFilesDir().getAbsolutePath();
        else
            this.WEB_ROOT = sdpath + "/" + this.DIR_NAME;
        if(!FileExtraUtils.createDir(this.WEB_ROOT)) {
            System.exit(0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServerRunner.stop();
    }



    private class ServiceBtnClickListen implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Button btn = (Button)view;
            String btnTxt = btn.getText().toString();
            TextView urlTxtView = (TextView)findViewById(R.id.url_adrr);
            if(btnTxt.equals(getResources().getString(R.string.start))){

                btn.setEnabled(false);
                String ipadrr;
                if(Utils.isWifiConnected(getApplicationContext()) && (ipadrr = Utils.getLocalIpAddress()) != null){
                    urlTxtView.setText("http://" + ipadrr + ":" + PORT);
                    ServerRunner.start(WEB_ROOT, PORT);
                    btn.setText(getResources().getString(R.string.stop));
                } else
                    urlTxtView.setText(getResources().getString(R.string.url_addr_nowifi));
                btn.setEnabled(true);
            } else {

                btn.setEnabled(false);
                ServerRunner.stop();
                btn.setText(getResources().getString(R.string.start));
                urlTxtView.setText(R.string.url_addr_init);
                btn.setEnabled(true);
            }
        }
    }
}
