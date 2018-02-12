package mdp2016.pmdp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.opencv.android.JavaCameraView;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements CvCameraViewListener2{
    // Used for logging success or failure messages
    private static final String TAG = "OCVSample::Activity";

    // Loads camera view of OpenCV for us to use. This lets us see using OpenCV
    private CameraBridgeViewBase mOpenCvCameraView;

    // Used in Camera selection from menu (when implemented)
    private boolean              mIsJavaCamera = true;
    private MenuItem             mItemSwitchCamera = null;

    // These variables are used (at the moment) to fix camera orientation from 270degree to 0degree
    private Mat mRgba,mRgbaF,mini,mSepiaKernel;
    private int MColor = 1;
    private int MFilter = 1;
    private boolean shown1 = true;
    private boolean shown2,shown3 = false;
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    // View variables
    private SeekBar sb;
    private LinearLayout mainLayout,mainLayout2,mainLayout3;
    private Button b11,b12,b13,b14,b21,b22;
    private FloatingActionButton br,fl,col,cap,gl;

    public MainActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        //* Hides Notification Bar
        //Set Activity Windows into Fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.show_camera);

        //Declare view for top bars
        sb = (SeekBar)findViewById(R.id.seekBar1);
        //Hide top bars, except one bar
        mainLayout = (LinearLayout)this.findViewById(R.id.navbar);
        mainLayout3=(LinearLayout)this.findViewById(R.id.navbar3);
        mainLayout3.setVisibility(LinearLayout.GONE);
        mainLayout2=(LinearLayout)this.findViewById(R.id.navbar2);
        mainLayout2.setVisibility(LinearLayout.GONE);

        //Set Open CV's camera view
        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.show_camera_activity_java_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        //declare procedures
        tekanFilterAtas();
        tekanFloatKiri();
        captureCamera();
        openGallery();
        //setContentView(R.layout.est);
    }
    //Click events for Top Positioned Buttons
    public void tekanFilterAtas(){
        b11 = (Button) findViewById(R.id.button11);
        b11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MColor = 1;
            }
        });
        b12 = (Button) findViewById(R.id.button12);
        b12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MColor = 2;
            }
        });
        b13 = (Button) findViewById(R.id.button13);
        b13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MColor = 3;
            }
        });
        b14 = (Button) findViewById(R.id.button14);
        b14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MColor = 4;
            }
        });
        b21 = (Button) findViewById(R.id.button21);
        b21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MFilter = 1;
            }
        });
        b22 = (Button) findViewById(R.id.button22);
        b22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MFilter = 2;
            }
        });
    }
    //Click events for Left Positioned Buttons
    //Trigger animation for top bars
    public void tekanFloatKiri(){
        br = (FloatingActionButton) findViewById(R.id.brightfilter);
        br.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAllBar();
                mainLayout3 = (LinearLayout) findViewById(R.id.navbar3);
                shown3 = !shown3;
                shown1 = false;
                shown2 = false;
                if(shown3){
                    mainLayout3.setVisibility(View.VISIBLE);
                    mainLayout3.setAlpha(0.0f);
                    mainLayout3.animate()
                            .translationY(0)
                            .alpha(1.0f);
                }
                else{
                    mainLayout3.animate()
                            .translationY(0)
                            .alpha(0.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                }
                            });
                }
            }
        });
        col = (FloatingActionButton) findViewById(R.id.colorfilter);
        col.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAllBar();
                mainLayout2=(LinearLayout)findViewById(R.id.navbar2);
                shown2 = !shown2;
                shown1 = false;
                shown3 = false;
                if(shown2){
                    mainLayout2.setVisibility(View.VISIBLE);
                    mainLayout2.setAlpha(0.0f);
                    mainLayout2.animate().translationY(-100).setDuration(300);
                    mainLayout2.animate()
                            .translationY(0)
                            .alpha(1.0f);
                }
                else{
                    mainLayout2.animate()
                            .translationY(-100)
                            .alpha(0.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                }
                            });
                }
            }
        });
        fl = (FloatingActionButton) findViewById(R.id.filter);
        fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAllBar();
                //LinearLayout mainLayout1=(LinearLayout)findViewById(R.id.navbar);
                shown1 = !shown1;
                shown2 = false;
                shown3 = false;
                if(shown1){
                    mainLayout.setVisibility(View.VISIBLE);
                    mainLayout.setAlpha(0.0f);
                    mainLayout.animate().translationY(-100).setDuration(300);
                    mainLayout.animate()
                            .translationY(0)
                            .alpha(1.0f);
                }
                else{
                    mainLayout.animate()
                            .translationY(-100)
                            .alpha(0.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                }
                            });
                }
            }
        });
    }
    //Hide all top bar
    public void hideAllBar(){
        mainLayout=(LinearLayout)this.findViewById(R.id.navbar);
        mainLayout.setVisibility(LinearLayout.GONE);
        mainLayout2=(LinearLayout)this.findViewById(R.id.navbar2);
        mainLayout2.setVisibility(LinearLayout.GONE);
        mainLayout3=(LinearLayout)this.findViewById(R.id.navbar3);
        mainLayout3.setVisibility(LinearLayout.GONE);
    }

    //Capture event
    //Trigger save image
    //Open Preview Activity after saving image
    public void captureCamera(){

        cap = (FloatingActionButton) findViewById(R.id.capture);
        cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Bitmap bmp = Bitmap.createBitmap(mini.cols(), mini.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(mini, bmp);

                new Thread(new Runnable() {
                    public void run() {
                        try {
                            String filename = "";
                            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                            try {
                                Date date = new Date();
                                filename = dateformat.format(date);
                                System.out.println("isa");
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                System.out.println("GAGAL");
                            }
                            saveImage(bmp, filename);
                            Intent intent = new Intent(MainActivity.this, PreviewActivity.class);
                            intent.putExtra("FN",filename);
                            startActivity(intent);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
    }
    //Open Gallery Intent
    public void openGallery(){
        gl = (FloatingActionButton) findViewById(R.id.gallery);
        gl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                startActivity(intent);
            }
        });
    }
    //Save image into CamCV folder in internal/external file
    public void saveImage(Bitmap bm, String filename){

        String root = Environment.getExternalStorageDirectory().getPath().toString();
        File myDir = new File(root+"/CamCV");
        myDir.mkdirs();
        File file = new File(myDir, filename+".jpg");
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }
    @Override
    public void onCameraViewStarted(int width, int height) {

        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mRgbaF = new Mat(height, width, CvType.CV_8UC4);

        // Fill sepia kernel
        mSepiaKernel = new Mat(4, 4, CvType.CV_32F);
        mSepiaKernel.put(0, 0, /* R */0.189f, 0.769f, 0.393f, 0f);
        mSepiaKernel.put(1, 0, /* G */0.168f, 0.686f, 0.349f, 0f);
        mSepiaKernel.put(2, 0, /* B */0.131f, 0.534f, 0.272f, 0f);
        mSepiaKernel.put(3, 0, /* A */0.000f, 0.000f, 0.000f, 1f);
    }

    @Override
    public void onCameraViewStopped() {
        //Release all Mats to prevent memory leak
        mRgba.release();
        mRgbaF.release();
        mSepiaKernel.release();
        mini.release();
    }

    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        // TODO Auto-generated method stub
        mRgba = inputFrame.rgba();


        //Change brightness based on seekbar value
        int brightness = sb.getProgress()-100;
        mRgba.convertTo(mRgba,-1,1,brightness);

        //Apply Sepia Color
        if(MColor == 2){
            Core.transform(mRgba, mRgba, mSepiaKernel);
        }
        //Apply GrayScale Color
        else if(MColor == 3){
            Imgproc.cvtColor(mRgba,mRgba,Imgproc.COLOR_RGB2GRAY);
        }
        //Apply Negative Color
        else if(MColor == 4){
            Core.bitwise_not(mRgba,mRgba);
        }

        if(MFilter == 2){
            Mat mIntermediateMat = new Mat();
            Imgproc.Canny(mRgba, mIntermediateMat, 80, 90);
            mRgba.setTo(new Scalar(0, 0, 0, 255), mIntermediateMat);
            Core.convertScaleAbs(mRgba, mIntermediateMat, 1./16, 0);
            Core.convertScaleAbs(mIntermediateMat, mRgba, 16, 0);
        }

        //Rotate camera to adjust phone rotation
        switch (mOpenCvCameraView.getDisplay().getRotation()) {

            case Surface.ROTATION_90: // 90° anti-clockwise
                break;

            case Surface.ROTATION_270: // 90° clockwise
                Imgproc.resize(mRgba, mRgbaF, mRgbaF.size(), 0,0, 0);
                Core.flip(mRgbaF, mRgba, -1);
                break;
            default:
        }

        mini = mRgba.clone();
        return mini; // This function must return
    }

}
