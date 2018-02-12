package mdp2016.pmdp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by STEFANUSKB on 7/19/2016.
 */
public class PreviewActivity extends Activity {
    Bitmap bmp;
    public PreviewActivity() {    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set Activity Windows into Fullscreen
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.preview_capture);

        //Receive String data from previous intent (MainActivity)
        //FN contains image's filename
        Intent maini = getIntent();
        String fn = maini.getStringExtra("FN");

        //Save into internal memory with CamCV folder
        //File will be saved as JPEG image
        String root = Environment.getExternalStorageDirectory().getPath().toString();
        File myDir = new File(root+"/CamCV");
        myDir.mkdirs();
        File imgFile = new File(myDir, fn+".jpg");

        //Check if imgFile is exist to prevent error
        //Set imageView with imgFile
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView myImage = (ImageView) findViewById(R.id.bitcap);
            myImage.setImageBitmap(myBitmap);
        }

        keluar();

    }

    //Same as back button event, finish intent
    public void keluar(){
        FloatingActionButton cap = (FloatingActionButton) findViewById(R.id.close);
        cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
