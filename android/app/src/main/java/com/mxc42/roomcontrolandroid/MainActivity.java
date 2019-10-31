package com.mxc42.roomcontrolandroid;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mxc42.roomcontrolandroid.model.MainModel;
import com.mxc42.roomcontrolandroid.model.RoomRequest;
import com.mxc42.roomcontrolandroid.model.RoomResponse;
import com.mxc42.roomcontrolandroid.net.ServerProxy;

public class MainActivity extends AppCompatActivity {

    private ToggleButton hallLightButton;
    private ToggleButton mainLightButton;
    private ToggleButton heatingBlanketButton;
    private Vibrator vibrator;
    private ServerProxy serverProxy;
    private ToggleButton[] portButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        portButtons = new ToggleButton[9];

        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        serverProxy = new ServerProxy();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        hallLightButton = (ToggleButton) findViewById(R.id.hall_light);
        portButtons[0] = hallLightButton;
        RoomResponse response = serverProxy.run(new RoomRequest("/read/0"));
        hallLightButton.setChecked(response.getState());
        hallLightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(70);
                RoomResponse response = serverProxy.run(new RoomRequest("/toggle/0"));
                hallLightButton.setChecked(response.getState());
                displayToast(response);
            }
        });

        mainLightButton = (ToggleButton) findViewById(R.id.main_light);
        portButtons[8] = mainLightButton;
        response = serverProxy.run(new RoomRequest("/read/8"));
        mainLightButton.setChecked(response.getState());
        mainLightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(70);
                RoomResponse response = serverProxy.run(new RoomRequest("/toggle/8"));
                mainLightButton.setChecked(response.getState());
                displayToast(response);
            }
        });

        heatingBlanketButton = (ToggleButton) findViewById(R.id.heating_blanket);
        portButtons[2] = heatingBlanketButton;
        response = serverProxy.run(new RoomRequest("/read/2"));
        heatingBlanketButton.setChecked(response.getState());
        heatingBlanketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(70);
                RoomResponse response = serverProxy.run(new RoomRequest("/toggle/2"));
                heatingBlanketButton.setChecked(response.getState());
                displayToast(response);
            }
        });
    }

    private void setButtonStates() {
        for (int i = 0; i < portButtons.length; i++) {
            if (portButtons[i] != null) {
                portButtons[i].setChecked(MainModel.getPort(i));
            }
        }
    }

    private void displayToast(RoomResponse response) {
        String toastMessage = null;
        if (response != null) {
            toastMessage = "Turned port " + response.getPort() + ' ' + response.getValue() + '.';
        }
        else {
            toastMessage = "Couldn't connect, RIP";
        }
        Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
    }
    private class RoomTask extends AsyncTask<RoomRequest, Boolean, Boolean> {
        private String errorToastText;

        @Override
        protected Boolean doInBackground(RoomRequest... requests) {
            ServerProxy serverProxy = new ServerProxy();

            RoomResponse response = serverProxy.run(requests[0]);

            if (response != null) {
                MainModel.setPort(response.getPort(), response.getState());
                return true;
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {

            }
            else {
                //Toast.makeText(getActivity(), errorToastText, Toast.LENGTH_LONG).show();
            }
        }
    }
}
