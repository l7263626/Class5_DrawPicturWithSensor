package tw.idv.chunhsin.class5_drawpicturwithsensor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView image;
    SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        findviews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        Sensor sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        int delay=SensorManager.SENSOR_DELAY_UI;
        sensorManager.registerListener(sensorEventListener,sensor,delay);
        */
    }

    @Override
    protected void onPause() {
        super.onPause();
        //sensorManager.unregisterListener(sensorEventListener);
    }

    void findviews(){
        image=(ImageView)findViewById(R.id.imageView);
    }
    boolean init=true;
    float initDegree;
    SensorEventListener sensorEventListener=new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float[] values=sensorEvent.values;
            //showCompass(values);
            if(init){
                initDegree = values[0];
                init = false;
            }
            showPhoneState(values);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };


    public void onDrawImageView(View v){
        Sensor sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        int delay=SensorManager.SENSOR_DELAY_NORMAL;
        sensorManager.registerListener(sensorEventListener,sensor,delay);

    }
    public void onDrawSurfaceView(View v){
        Intent intent = new Intent(MainActivity.this,SurfaceViewActivity.class);
        startActivity(intent);
    }

    void showPhoneState(float[] values){
        Bitmap bmpPlate= BitmapFactory.decodeResource(getResources(), R.drawable.androidplate);
        Bitmap bmpWidth= BitmapFactory.decodeResource(getResources(), R.drawable.androidwidth);
        Bitmap bmpHeight= BitmapFactory.decodeResource(getResources(), R.drawable.androidheight);
        int bmp_w=image.getWidth();
        int bmp_h=image.getHeight();
        Bitmap bmp=Bitmap.createBitmap(bmp_w,bmp_h, Bitmap.Config.ARGB_8888);

        Paint paint=new Paint();
        paint.setAntiAlias(true);

        Canvas canvas=new Canvas(bmp);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bmpPlate, bmp_w / 2 - bmpPlate.getWidth() / 2, bmp_h / 4 - bmpPlate.getHeight() / 2, paint);
        canvas.drawBitmap(bmpHeight, bmp_w / 2 - bmpHeight.getWidth() / 2, bmp_h / 2 - bmpHeight.getHeight() / 2, paint);
        canvas.drawBitmap(bmpWidth, bmp_w / 2 - bmpWidth.getWidth() / 2, (bmp_h / 4) * 3 - bmpWidth.getHeight() / 2, paint);
        canvas.save();
        canvas.drawColor(Color.WHITE);
        canvas.rotate(values[0] - initDegree, bmp_w / 2, bmp_h / 4);
        canvas.drawBitmap(bmpPlate, bmp_w / 2 - bmpPlate.getWidth() / 2, bmp_h / 4 - bmpPlate.getHeight() / 2, paint);
        canvas.restore();
        canvas.drawText(String.valueOf(values[0]-initDegree), 50, bmp_h / 4, paint);
        canvas.save();
        canvas.rotate(values[1], bmp_w / 2, bmp_h / 2);
        canvas.drawBitmap(bmpHeight, bmp_w / 2 - bmpHeight.getWidth() / 2, bmp_h / 2 - bmpHeight.getHeight() / 2, paint);
        canvas.restore();
        canvas.drawText(String.valueOf(values[1]), 50, bmp_h / 2, paint);

        canvas.save();
        canvas.rotate(-values[2], bmp_w / 2, bmp_h / 4 * 3);
        canvas.drawBitmap(bmpWidth, bmp_w / 2 - bmpWidth.getWidth() / 2, bmp_h / 4 * 3 - bmpWidth.getHeight() / 2, paint);
        canvas.restore();
        canvas.drawText(String.valueOf(values[2]), 50, bmp_h / 4*3, paint);

        image.setImageBitmap(bmp);
    }

    void showCompass(float[] values){
        Bitmap bmpCompass= BitmapFactory.decodeResource(getResources(), R.drawable.compass);
        int bmp_w=image.getWidth();
        int bmp_h=image.getHeight();
        Bitmap bmp=Bitmap.createBitmap(bmp_w,bmp_h, Bitmap.Config.ARGB_8888);

        Paint paint=new Paint();
        paint.setAntiAlias(true);

        Canvas canvas=new Canvas(bmp);
        canvas.drawColor(Color.WHITE);

        canvas.drawBitmap(bmpCompass, bmp_w / 2 - bmpCompass.getWidth() / 2, bmp_h / 2 - bmpCompass.getHeight() / 2, paint);
        canvas.save();
        canvas.drawColor(Color.WHITE);
        canvas.rotate(values[0], bmp_w / 2, bmp_h / 4);
        canvas.drawBitmap(bmpCompass, bmp_w / 2 - bmpCompass.getWidth() / 2, bmp_h / 2 - bmpCompass.getHeight() / 2, paint);
        canvas.restore();
        canvas.save();
        canvas.rotate(values[0], bmp_w / 2, bmp_h / 4*3);

        canvas.restore();
        image.setImageBitmap(bmp);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
