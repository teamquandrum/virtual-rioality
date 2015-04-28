package com.quandrum.atos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.quandrum.Json.FilePathSetter;
import com.quandrum.atos.R;

public class TakePictureActivity extends Activity implements OnClickListener{

	TextView error;
	Camera mCamera;
	//Uri fr;
	Handler mHandler;
	Uri npath;
	int w;
	int h;
	private PictureCallback mPicCall = new PictureCallback(){

		@Override
		public void onPictureTaken(byte[] data, Camera arg1) {
			FileOutputStream fo = null;
			mCamera.stopPreview();//stops displaying the camera view
			npath = FilePathSetter.getNextImagePath(TakePictureActivity.this);//gets the path to store newly taken image
			File f = new File(npath.getEncodedPath());
			try{
				fo = new FileOutputStream(f);
				fo.write(data);//writes the image data to the file
				mHandler.sendMessage(mHandler.obtainMessage());
			}catch(Exception e)
			{
				
			}
			finally{
				try {
					fo.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Display display = getWindowManager().getDefaultDisplay();
		w=display.getWidth();
		h=display.getHeight();
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		error = new TextView(this);//used in case an error crops up which needs to be displayed
		RelativeLayout mrl = new RelativeLayout(this);
		setContentView(mrl);
		mrl.addView(error);
		//fr = Uri.fromFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES));
		mCamera = getCameraInstance();//user-defined function, see below
		mCamera.setDisplayOrientation(90);
		mHandler = new Handler(){
			public void handleMessage(Message msg)
			{
				Toast.makeText(TakePictureActivity.this, "Picture taken",Toast.LENGTH_SHORT).show();
				finish();
			}
		};
		if(mCamera==null)
		{
			error.setText("Camera is being used by another application.");
		}
		else//camera is available for the application to use
		{
			CameraViewer cv = new CameraViewer(this,mCamera);//see the CameraViewer class. It is a SurfaceView to display what the camera sees
			mrl.addView(cv);//Adds this surfaceview to the layout
			ImageButton tp = new ImageButton(this);//The take picture button
			tp.setImageResource(R.drawable.cam);
			mrl.addView(tp);
			RelativeLayout.LayoutParams tplp = (LayoutParams) tp.getLayoutParams();
			tplp.width=w;
			tplp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			tplp.addRule(RelativeLayout.CENTER_HORIZONTAL);
			tp.setId(1);
			tp.setOnClickListener(this);
		}
	}
	Camera getCameraInstance()
	{
		Camera c = null;
		try{
			c = Camera.open();
		}catch(Exception e)
		{
			
		}
		return c;//if camera opening failed, returns null
	}
	public void closeActivity()
	{
		this.onBackPressed();
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case 1:
		{
			mCamera.takePicture(null, null,mPicCall);//we only require to catch the jpeg image, hence only third argument is required	
			break;
		}		
		}	
	}
}
