package com.example.getgps;

import java.lang.reflect.Method;
import java.util.Random;
import java.lang.Math;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	private Button button01;
	private double latitude;
	private double longitude;
	private double r;
	private double angle;
	private Handler handler_1 = null;//����
	private HandlerThread handlerThread_1 = null;//���u
	private String handlerThread_1_name = "�ڬO1�����u";

	class GPSListener implements LocationListener {
		public void onLocationChanged(Location location) {
			// GPSStatus.append("location: lat:" + location.getLatitude() +
			// ", long"+ location.getLongitude() + "\r\n");
			Log.d("GPS", "location: lat:" + location.getLatitude() + ", long"
					+ location.getLongitude() + "\r\n");
			TextView GPSStatus = (TextView) findViewById(R.id.txt_message);
			GPSStatus.setText("location: lat:" + location.getLatitude()
					+ ", long" + location.getLongitude());
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

	}
	
	public void setMockLocation(double latitude, double longitude) 
	{
		
		
	    LocationManager testLM = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
	   if( testLM != null )
	   {
	      try
	      {
	         String mocLocationProvider = testLM.GPS_PROVIDER;
	         testLM.addTestProvider(mocLocationProvider, false, false, false, false, false, true, true, 0, 5);
	         testLM.setTestProviderEnabled(mocLocationProvider, true);
	         Location loc = new Location(mocLocationProvider);
	         loc.setTime(System.currentTimeMillis()); 
	         loc.setLatitude(latitude);
	         loc.setLongitude(longitude);
	         loc.setAltitude(0);
	         loc.setAccuracy(0);
	         Method method = Location.class.getMethod("makeComplete");  
	         if (method != null)  
	         {  
	             method.invoke(loc);  
	         }  
	         
	         testLM.setTestProviderLocation(mocLocationProvider, loc); 
	         //Toast.makeText(getApplicationContext(), "MockGPS Set", Toast.LENGTH_SHORT).show();
	      }
	      catch( Exception e )
	      {
	    	  e.printStackTrace();  
	         Toast.makeText(getApplicationContext(), "MockGPS Failed", Toast.LENGTH_SHORT).show();
	      }
	  }
	  else
	      Toast.makeText(getApplicationContext(), "No LocationManager", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button01 = (Button)findViewById(R.id.button1);
		latitude=25.0175;
		longitude=121.2992;
		angle = 0;
		r = 0.003;
		
		
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// GPSListener locationListenerNGPS = new GPSListener();
		GPSListener locationListenerGPS = new GPSListener();

		// locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
		// 0, 0, locationListenerNGPS);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, locationListenerGPS);

		button01.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {

			}
		});

		handlerThread_1 = new HandlerThread(handlerThread_1_name); // �s�W�@�ӭ��u
																	// ���L�@�ӦW�r
		handlerThread_1.start();// ���L�}�l�W�Z
		handler_1 = new Handler(handlerThread_1.getLooper());// �s�W�@�Ӧ��� �L�O���u1��������
		handler_1.post(runnable_1);// ����������u1���h����(runnable_1)
	}

	private Runnable runnable_1 = new Runnable() {
		public void run() {
			
			Random random = new Random();
			
			latitude +=  (random.nextInt(21) -10) * 0.000005; 
			longitude +=  (random.nextInt(21) -10) * 0.000005;
			Log.d("gps",String.valueOf(latitude)+" "+ String.valueOf(longitude));
			setMockLocation(latitude + r*Math.cos(angle), longitude+ r*Math.sin(angle));
			angle = angle + 0.003;

			// ������w�C�j�X��n���@���u�@1 (���@��:1000����1��)
			handler_1.postDelayed(this, 300);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		//Log.d("HelloWorldActivity", "���");
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
