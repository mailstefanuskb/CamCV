package mdp2016.pmdp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by STEFANUSKB on 7/28/2016.
 */
public class GalleryActivity extends Activity  {
    public GalleryActivity() {    }
    int pos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set Activity Windows into Fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.preview_gallery);

        //declare procedures
        getFromMemory();
        chImage();
        delfile();
        keluar();
        previ();
        nexti();
    }
    ArrayList<String> f = new ArrayList<String>();// list of file paths
    File[] listFile;

    //Get all file from certain directory into array of file
    public void getFromMemory()
    {
        String root = Environment.getExternalStorageDirectory().getPath().toString();
        File file = new File(root+"/CamCV");
        //File file= new File(android.os.Environment.getExternalStorageDirectory(),"TMyFolder");
        if (file.isDirectory())
        {
            listFile = file.listFiles();
            for (int i = listFile.length-1; i >= 0; i--)
            {
                f.add(listFile[i].getAbsolutePath());
            }
        }
    }
    //Same as back button
    public void keluar(){
        FloatingActionButton cap = (FloatingActionButton) findViewById(R.id.close);
        cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    //Change file index position
    //To decrease pos value
    //Continue to chImage
    public void previ(){
        FloatingActionButton cap = (FloatingActionButton) findViewById(R.id.prev);
        cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos--;
                if(pos<0)pos = f.size()-1;
                chImage();
            }
        });
    }
    //Change file index position
    //To increase pos value
    public void nexti(){
        FloatingActionButton cap = (FloatingActionButton) findViewById(R.id.next);
        cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos++;
                if(pos>=f.size())pos=0;
                chImage();
            }
        });
    }
    //Change myImage based on file position
    public void chImage(){
        if(f.size()>0){
            File imgFile = new File(f.get(pos));
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ImageView myImage = (ImageView) findViewById(R.id.bitgal);
                myImage.setImageBitmap(myBitmap);
            }
        }
    }
    //Procedure to trigger deletion alert by clicking Trashcan Button
    public void delfile(){
        FloatingActionButton cap = (FloatingActionButton) findViewById(R.id.delfile);
        cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog diaBox = AskOption();
                diaBox.show();
            }
        });
    }
    //Show Alert if Delete event happens
    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete?")
                .setIcon(R.drawable.trashcan)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        File fdelete = new File(f.get(pos));
                        if (fdelete.exists()) {
                            if (fdelete.delete()) {
                                finish();
                            }
                        }
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }
}
