package com.touchKin.touchkinapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.commonsware.cwac.camera.CameraHost;
import com.commonsware.cwac.camera.CameraUtils;
import com.commonsware.cwac.camera.PictureTransaction;
import com.commonsware.cwac.camera.SimpleCameraHost;

import com.ryanharter.android.tooltips.ToolTip;
import com.ryanharter.android.tooltips.ToolTipLayout;
import com.ryanharter.android.tooltips.ToolTip.Builder;
import com.commonsware.cwac.camera.CameraFragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.media.CamcorderProfile;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ryanharter.android.tooltips.ToolTip;
import com.ryanharter.android.tooltips.ToolTip.Builder;
import com.ryanharter.android.tooltips.ToolTipLayout;
import com.touchKin.touchkinapp.custom.AlbumStorageDirFactory;
import com.touchKin.touchkinapp.custom.BaseAlbumDirFactory;
import com.touchKin.touchkinapp.custom.CameraPreview;
import com.touchKin.touchkinapp.custom.FroyoAlbumDirFactory;


@SuppressWarnings("deprecation")
public class SendTouchActivity extends Activity implements
		DemoCameraFragment.Contract, OnClickListener {
	private DemoCameraFragment current = null;
	private DemoCameraFragment ffc = null;
	private DemoCameraFragment std = null;
	private boolean hasTwoCameras = (Camera.getNumberOfCameras() > 1);
	private Context myContext;
	private Camera mCamera;
	private Button switchCamera, imageCapture, videoMode, imageMode,
			menuButton;
	private ToggleButton videoCapture;
	RelativeLayout menuLayout;
	private Button backButton;
	private int cameraid;
	ToolTipLayout tipContainer;
	private boolean cameraFront = false;
	private boolean singleShot = true;
	private static final int CONTENT_REQUEST = 1337;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_touch);
		initialize();
	}

	public void initialize() {

		// cameraPreview = (FrameLayout) findViewById(R.id.container);
		// mPreview = new CameraPreview(myContext, mCamera);
		// cameraPreview.addView(mPreview);
		videoCapture = (ToggleButton) findViewById(R.id.video_capture_button);
		videoCapture.setOnClickListener(captrureListener);
		switchCamera = (Button) findViewById(R.id.change_camera_button);
		switchCamera.setOnClickListener(switchCameraListener);
		imageCapture = (Button) findViewById(R.id.take_picture_button);
		imageCapture.setOnClickListener(picturelistener);
		videoMode = (Button) findViewById(R.id.video_mode_button);
		videoMode.setOnClickListener(this);
		imageMode = (Button) findViewById(R.id.image_mode_button);
		imageMode.setOnClickListener(this);
		menuButton = (Button) findViewById(R.id.menuButton);
		menuButton.setOnClickListener(this);
		menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);
		backButton = (Button) findViewById(R.id.back_button);
		backButton.setOnClickListener(this);
		tipContainer = (ToolTipLayout) findViewById(R.id.tooltip_container);
		myContext = SendTouchActivity.this;

	}

	public void onResume() {
		super.onResume();
		if (!hasCamera(myContext)) {
			Toast toast = Toast.makeText(myContext,
					"Sorry, your phone does not have a camera!",
					Toast.LENGTH_LONG);
			toast.show();
			finish();
		}
		if (std == null) {
			std = DemoCameraFragment.newInstance(false);
			current = std;
		}
		// if (ffc == null) {
		// ffc=DemoCameraFragment.newInstance(true);
		// current=ffc;
		// }
		getFragmentManager().beginTransaction()
				.replace(R.id.container, current).commit();

	}

	private boolean hasCamera(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			return true;
		} else {
			return false;
		}
	}

	OnClickListener switchCameraListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (std != null) {
				ffc = DemoCameraFragment.newInstance(true);
				current = ffc;
				std = null;
			} else {
				std = DemoCameraFragment.newInstance(false);
				current = std;
				ffc = null;
			}

			getFragmentManager().beginTransaction()
					.replace(R.id.container, current).commit();

		}
	};

	OnClickListener picturelistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			current.takeSimplePicture();

		}
	};

	OnClickListener captrureListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				if (videoCapture.isChecked()) {

					current.record();

				} else {
					current.stopRecording();

				}

			} catch (Exception e) {
				Log.e(getClass().getSimpleName(), "Exception trying to record",
						e);
			}

		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.menuButton:
			openMenu();
			break;
		case R.id.video_mode_button:
			startVideoMode();
			break;
		case R.id.image_mode_button:
			startImageMode();
			break;
		case R.id.back_button:
			openDialog();
			break;
		case R.id.yesButton:
			goBack();
			break;
		case R.id.noButton:
			dismiss();
			break;
		default:
			break;
		}

	}

	private void startImageMode() {
		// TODO Auto-generated method stub
		menuLayout.setVisibility(View.INVISIBLE);
		menuButton.setVisibility(View.VISIBLE);
		imageCapture.setVisibility(View.VISIBLE);
	}

	private void startVideoMode() {
		// TODO Auto-generated method stub
		menuLayout.setVisibility(View.INVISIBLE);
		menuButton.setVisibility(View.VISIBLE);
		videoCapture.setVisibility(View.VISIBLE);
	}

	private void dismiss() {
		// TODO Auto-generated method stub
		tipContainer.dismiss(true);
	}

	private void goBack() {
		// TODO Auto-generated method stub
		// Intent intent = new Intent(this, DashBoardActivity.class);
		// startActivity(intent);
		// finish();
	}

	private void openDialog() {
		// TODO Auto-generated method stub
		RelativeLayout customLayout = (RelativeLayout) getLayoutInflater()
				.inflate(R.layout.discard_popup, null);

		// Create a content view however you'd like
		// ...
		TextView message = (TextView) customLayout.findViewById(R.id.message);
		message.setText("Go Back");
		Button noButton = (Button) customLayout.findViewById(R.id.noButton);
		Button yesButton = (Button) customLayout.findViewById(R.id.yesButton);
		noButton.setOnClickListener(this);
		yesButton.setOnClickListener(this);
		// Create a ToolTip using the Builder class
		// ToolTip t = new Builder(SendTouchActivity.this).anchor(backButton)
		// .gravity(Gravity.BOTTOM) // The location of the view in relation
		// .dismissOnTouch(false) // to the anchor (LEFT, RIGHT, TOP,
		// // BOTTOM)
		// .color(Color.WHITE) // The color of the pointer arrow
		// .pointerSize(30) // The size of the pointer
		// .contentView(customLayout) // The actual contents of the ToolTip
		// .build();
		//
		// tipContainer.addTooltip(t);
	}

	private void openMenu() {
		// TODO Auto-generated method stub
		// tipContainer.dismiss(true);
		videoCapture.setVisibility(View.INVISIBLE);
		menuButton.setVisibility(View.INVISIBLE);
		menuLayout.setVisibility(View.VISIBLE);
		imageCapture.setVisibility(View.INVISIBLE);
	}

	@Override
	public boolean isSingleShotMode() {
		// TODO Auto-generated method stub
		return (singleShot);
	}

	@Override
	public void setSingleShotMode(boolean mode) {
		// TODO Auto-generated method stub
		singleShot = mode;
	}

	Contract getContract() {
		return ((Contract) this);
	}

	interface Contract {
		boolean isSingleShotMode();

		void setSingleShotMode(boolean mode);
	}

}
