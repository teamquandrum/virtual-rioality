package com.quandrum.atos;

import com.quandrum.atos.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainMenuActivity extends Activity implements OnClickListener{
	
	Uri fr;
	int w;
	int h;
	ImageView tpimg;
	ImageView hlpimg;
	ImageView vgimg;
	ImageView calimg;
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); 
        
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        h = display.getHeight();
        w = display.getWidth();
        
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        
        final RelativeLayout mrl = new RelativeLayout(this);
        mrl.setBackgroundColor(getResources().getColor(R.color.white));
        ScrollView sc = new ScrollView(this);//To allow scrolling of buttons in rl
        RelativeLayout.LayoutParams sclp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        sclp.addRule(RelativeLayout.BELOW,2);//Below title
        sc.setLayoutParams(sclp);
        //Creating children of mrl relativelayout
        setContentView(mrl);
        
        
        LinearLayout rl = new LinearLayout(this);
        rl.setOrientation(LinearLayout.VERTICAL);
        mrl.addView(rl);
        
        //Creating children of rl relativelayout
        
        //Take Picture button        
        RelativeLayout tp = new RelativeLayout(this);
        tp.setId(3);        
        LinearLayout.LayoutParams tplp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,0);
        tplp.weight=1;
        tp.setLayoutParams(tplp);
        tp.setOnClickListener(this);
        rl.addView(tp);    
        tplp.bottomMargin=SizeManager.getDip(5, metrics);
                
        tpimg = new ImageView(this);
        tp.addView(tpimg);
        RelativeLayout.LayoutParams tpimglp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        tpimg.setLayoutParams(tpimglp);
        tpimg.setScaleType(ScaleType.CENTER_INSIDE);
        tpimg.setImageResource(R.drawable.scheduler);
       
        TextView tptv = new TextView(this);
        tptv.setText("Scheduler");
        tptv.setTextColor(getResources().getColor(R.color.black));
        tptv.setTypeface(null,Typeface.BOLD);
        tp.addView(tptv);
        RelativeLayout.LayoutParams tptvlp = (LayoutParams) tptv.getLayoutParams();
        tptvlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        
        //View gallery button (shows all images)
        RelativeLayout vg = new RelativeLayout(this);
        vg.setId(4);
        LinearLayout.LayoutParams vglp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,0);
        vglp.weight=1;
        vg.setLayoutParams(vglp);
        vg.setOnClickListener(this);
        rl.addView(vg);
        vglp.bottomMargin=SizeManager.getDip(5, metrics);
        
        vgimg = new ImageView(this);
        vgimg.setScaleType(ScaleType.CENTER_INSIDE);
        RelativeLayout.LayoutParams vgimglp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        vgimg.setLayoutParams(vgimglp);
        vgimg.setImageResource(R.drawable.augmentedrealityicon);
        vg.addView(vgimg);
        
        TextView vgtv = new TextView(this);
        vgtv.setText("Enchanced Reality");
        vgtv.setTextColor(getResources().getColor(R.color.black));
        vgtv.setTypeface(null,Typeface.BOLD);
        vg.addView(vgtv);
        RelativeLayout.LayoutParams vgtvlp = (LayoutParams) vgtv.getLayoutParams();
        vgtvlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        
        //Calendar button
        RelativeLayout calbt = new RelativeLayout(this);
        calbt.setId(5);
        LinearLayout.LayoutParams callp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,0);        
        calbt.setLayoutParams(callp);
        calbt.setOnClickListener(this);
        rl.addView(calbt);
        callp.bottomMargin=SizeManager.getDip(5, metrics);
        callp.weight=1;
        
        calimg = new ImageView(this);
        RelativeLayout.LayoutParams calimglp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        calimg.setLayoutParams(calimglp);
        calimg.setScaleType(ScaleType.CENTER_INSIDE);
        calimg.setImageResource(R.drawable.translate);
        calbt.addView(calimg);
        
        TextView caltv = new TextView(this);
        caltv.setText("Translate Image And Speech");
        caltv.setTypeface(null,Typeface.BOLD);
        caltv.setTextColor(getResources().getColor(R.color.black));
        calbt.addView(caltv);
        RelativeLayout.LayoutParams caltvlp = (LayoutParams) caltv.getLayoutParams();
        caltvlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        
        
        //"About" button
        RelativeLayout help = new RelativeLayout(this);
        help.setId(6);
        LinearLayout.LayoutParams helplp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,0);        
        help.setLayoutParams(helplp);
        help.setOnClickListener(this);
        rl.addView(help);
        helplp.bottomMargin=SizeManager.getDip(5, metrics);
        helplp.weight=1;
        
        hlpimg = new ImageView(this);
        RelativeLayout.LayoutParams hlpimglp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        hlpimg.setLayoutParams(hlpimglp);
        hlpimg.setScaleType(ScaleType.CENTER_INSIDE);
        hlpimg.setImageResource(R.drawable.camera);
        help.addView(hlpimg);
        
        TextView hlptv = new TextView(this);
        hlptv.setText("Take Picture");
        hlptv.setTypeface(null,Typeface.BOLD);
        hlptv.setTextColor(getResources().getColor(R.color.black));
        help.addView(hlptv);
        RelativeLayout.LayoutParams hlptvlp = (LayoutParams) hlptv.getLayoutParams();
        hlptvlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        
    }

    @Override
    public void onClick(View v)
    {
    	RelativeLayout temprl = (RelativeLayout)findViewById(v.getId());
    	doLayoutAnimation(temprl);
    }
    public void doLayoutAnimation(final RelativeLayout temprl)
    {
    	AnimationListener animlstnr = new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation arg0) {
				//starts the new activity at the end of the animation
				switch(temprl.getId())
				{
				case 3: //Scheduler button
				{
		    		//doChangeScreenAnimation();    	
					//AMAN TODO uncomment below block and set correct name of SchedulerActivity
		    		Intent i = new Intent(getBaseContext(),com.example2.schedulerui.MainActivity.class);
		    		startActivity(i);
		    		break;    		
		    	}
		    	case 4: //Augmented Reality Button
		    	{
		    		//doChangeScreenAnimation();
		    		Intent i = new Intent(getBaseContext(),AugmentedRealityActivity.class);
		    		startActivity(i);
		    		break;
		    	}
		    	case 5: //Translate Button
		    	{
		    		//doChangeScreenAnimation();
		    		Intent i = new Intent();
		    		i.setAction(Intent.ACTION_VIEW);
		    		i.setComponent(new ComponentName("com.google.android.apps.translate",
		                    "com.google.android.apps.translate.TranslateActivity"));
		    		startActivity(i);
		    		break;
		    	}
		    	case 6: //Take picture button
		    	{
		    		//PathFinder.emptyDirectory(fr);
		    		Intent i = new Intent(getBaseContext(),com.quandrum.atos.Intermediate.class);//use again		    		
		    		startActivity(i);
		    		break;
		    	}
				
				}
			}
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub - we don't require this function
				
			}
			@Override
			public void onAnimationStart(Animation arg0) {
		    	temprl.setOnClickListener(null);//prevents user accidentally clicking again before animation ends
			}    		
    	};
    	Animation translation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.ABSOLUTE,w,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0);
    	translation.setDuration(500);
    	translation.setFillAfter(true);//After animation, view will shift to its new location
    	translation.setFillBefore(true);//Before animation, view will start its transformation
    	temprl.startAnimation(translation);
    	translation.setAnimationListener(animlstnr);
    }
    @Override
    public void onResume(){
    	super.onResume();
    	onCreate(null);
    }
}
