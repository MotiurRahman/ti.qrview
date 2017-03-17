/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package ti.qrview;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.view.TiCompositeLayout;
import org.appcelerator.titanium.view.TiCompositeLayout.LayoutArrangement;
import org.appcelerator.titanium.view.TiUIView;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.app.Activity;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView.OnQRCodeReadListener;
import android.view.View;
import android.graphics.PointF;
import android.Manifest;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import org.appcelerator.kroll.KrollFunction;
import java.util.HashMap;

@Kroll.proxy(creatableInModule=TiQRViewModule.class)
public class QRViewProxy extends TiViewProxy implements OnQRCodeReadListener
{
	// Standard Debugging variables
	private static final String LCAT = "QRViewProxy";
	private static final boolean DBG = TiConfig.LOGD;
	private QRCodeReaderView mydecoderview = null;
	private PointsOverlayView pointsOverlayView;
	private Boolean torch = false;
	private Boolean useFrontCamera = false;
	private KrollFunction callbackFount;
	
	@Kroll.constant public static final int CAMERA_BACK = 0;
	@Kroll.constant public static final int CAMERA_FRONT = 1;
	 
	private class QRCodeView extends TiUIView
	{
		public QRCodeView(TiViewProxy proxy) {
			super(proxy);
			
			RelativeLayout rlmain = new RelativeLayout(TiApplication.getInstance());
			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
			rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			RelativeLayout.LayoutParams rlp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
			
			
			mydecoderview = new QRCodeReaderView(TiApplication.getInstance());
			pointsOverlayView = new PointsOverlayView(TiApplication.getInstance());
			
			if (useFrontCamera){
				mydecoderview.setFrontCamera();
			} else {
				mydecoderview.setBackCamera();
			}
			mydecoderview.setQRDecodingEnabled(true);			
			rlmain.addView(mydecoderview,rlp);
			rlmain.addView(pointsOverlayView,rlp2);
			setNativeView(rlmain);
		}

		@Override
		public void processProperties(KrollDict d)
		{
			super.processProperties(d);
		}
	}
	
	private class BlankView extends TiUIView
	{
		public BlankView(TiViewProxy proxy) {
			super(proxy);
			
			LinearLayout rlmain = new LinearLayout(TiApplication.getInstance());		
			setNativeView(rlmain);
		}

		@Override
		public void processProperties(KrollDict d)
		{
			super.processProperties(d);
		}
	}


	// Constructor
	public QRViewProxy()
	{
		super();
	}

	@Override
	public TiUIView createView(Activity activity)
	{		
		int result = activity.checkCallingOrSelfPermission(Manifest.permission.CAMERA);
		if (result >-1){
			TiUIView view = new QRCodeView(this);
			view.getLayoutParams().autoFillsHeight = true;
			view.getLayoutParams().autoFillsWidth = true;
			if (mydecoderview != null){
				mydecoderview.setOnQRCodeReadListener(this);
			}
			return view;
		} else {
			Log.e(LCAT,"Camera permission needed");
			TiUIView view = new BlankView(this);
			return view;
		}
	}

	// Handle creation options
	@Override
	public void handleCreationDict(KrollDict options)
	{
		super.handleCreationDict(options);

		if (options.containsKey("useFrontCamera")) {
			useFrontCamera = options.getBoolean("useFrontCamera");
		}
		if (options.containsKey("autoFocusInterval")) {
			if (mydecoderview != null){
				mydecoderview.setAutofocusInterval(options.getInt("autoFocusInterval"));
			}
		}
	}
	
	// Methods
	@Kroll.method
	public void startCamera()
	{
		if (mydecoderview != null){
			mydecoderview.surfaceCreated(mydecoderview.getHolder());
			mydecoderview.startCamera();
		}
	}
	
	// Methods
	@Kroll.method
	public void stopCamera()
	{
		if (mydecoderview != null){
			mydecoderview.stopCamera();
		}
	}
	
	
	@Kroll.setProperty @Kroll.method
	public void setTorch(Boolean val)
	{
		torch = val;
		mydecoderview.setTorchEnabled(val);
	}
	@Kroll.getProperty @Kroll.method
	public Boolean getTorch()
	{
		return torch;
	}
	@Kroll.method
	public void toggleTorch()
	{
		torch = !torch;
		mydecoderview.setTorchEnabled(torch);
	}
	@Kroll.method
	public void forceAutoFocus()
	{
		mydecoderview.forceAutoFocus();
	}
	
	@Kroll.setProperty @Kroll.method
	public void setCamera(int cam) {
		// TODO not working
		if (cam == CAMERA_FRONT) {
			Log.i(LCAT, "Set front camera");
			mydecoderview.setFrontCamera();
		} else {
			Log.i(LCAT, "Set back camera");
			mydecoderview.setBackCamera();
		}
	}
	
	@Kroll.method
	public void addEventListener(String evt, KrollFunction kf) {
		if (evt.equals("found")) {
			callbackFount =(KrollFunction) kf;
		}
	}


    @Override
    public void onQRCodeRead(String text, PointF[] points) {
		pointsOverlayView.setPoints(points);
		if (callbackFount != null) {
			HashMap<String,Object> event = new HashMap<String, Object>();
			event.put("text", text);
 			callbackFount.call(getKrollObject(), event);
		}
    }

	// @Override
	// protected void onResume() {
	// 	super.onResume();
	// 	mydecoderview.startCamera();
	// }
	// 
	// @Override
	// protected void onPause() {
	// 	super.onPause();
	// 	mydecoderview.stopCamera();
	// }
}
